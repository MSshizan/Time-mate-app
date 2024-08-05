package com.example.clock.Presentation.ViewModels

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clock.dataLayer.receiver.NotificationReceiver
import com.example.clock.domainLayer.Reminder
import com.example.clock.api.HolidayApiService
import com.example.clock.domainLayer.entity.HolidayEntity
import com.example.clock.dataLayer.dao.HolidayDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.YearMonth
import java.util.Calendar

class CalenderViewModel(
    private val apiService : HolidayApiService,
    private val holidayDao: HolidayDao,
    private val reminderViewModel: ReminderViewModel,
    private val context: Context

):ViewModel() {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)

    private val _selectedCountry = MutableLiveData<String>()
    val selectedCountry: LiveData<String> get() = _selectedCountry

    private val scheduledNotifications = mutableSetOf<Pair<LocalDate, String>>()
    private val _holidays = MutableStateFlow<Map<LocalDate, List<HolidayEntity>>>(emptyMap())
    val holidays: StateFlow<Map<LocalDate, List<HolidayEntity>>> = _holidays


    private val _reminders = MutableStateFlow<List<Reminder>>(emptyList())
    val reminders: StateFlow<List<Reminder>> = _reminders

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        viewModelScope.launch {
            fetchAllHolidays("US", LocalDate.now().year)
            fetchReminders()
            _selectedCountry.value = sharedPreferences.getString("selected_country", "US")
        }
    }

    fun setSelectedCountry(countryCode: String) {
        _selectedCountry.value = countryCode
        sharedPreferences.edit().putString("selected_country", countryCode).apply()
        fetchAllHolidays(countryCode, YearMonth.now().year)
    }

    fun fetchAllHolidays(countryCode: String, year: Int) {
        viewModelScope.launch {
            _errorMessage.value = null
            val holidaysFromDb = withContext(Dispatchers.IO) {
                getHolidaysFromDatabase(countryCode, year)
            }



            if (holidaysFromDb.isNotEmpty()) {
                _holidays.value = holidaysFromDb
                holidaysFromDb.forEach { (date, holidays) ->
                    if (date.isAfter(LocalDate.now()) || date.isEqual(LocalDate.now())) {
                        holidays.forEach { holiday ->
                            scheduleNotification(date, holiday.name, "It's ${holiday.name} today!")
                        }
                    }
                }
                Log.d("CalenderViewModel", "Loaded holidays from database for year $year")
            } else {
                // Fetch from API if not present in database
                try {
                    val response = apiService.getHolidays(year = year, countryCode = countryCode)
                    val holidayMap = response.groupBy { LocalDate.parse(it.date) }
                        .mapValues { entry ->
                            entry.value.map { holiday ->
                                HolidayEntity(
                                    id = holiday.name.hashCode(),
                                    name = holiday.name,
                                    date = holiday.date,
                                    country = countryCode
                                )
                            }
                        }

                    _holidays.value = holidayMap


                    withContext(Dispatchers.IO) {
                        saveHolidaysToDatabase(holidayMap)
                    }

                    Log.d("CalenderViewModel", "Fetched and saved holidays for year $year")





                } catch (e: Exception) {
                    e.printStackTrace()
                    _errorMessage.value = "No internet connection to update holiday"
                    Log.e("CalenderViewModel", "Error fetching holidays", e)
                    Log.d("CalenderViewModel", "Holidays processed: ${_holidays.value}")
                }
            }
        }
    }




    fun getRemindersForMonth(yearMonth: YearMonth): List<Reminder> {
        val startOfMonth = yearMonth.atDay(1)
        val endOfMonth = yearMonth.atEndOfMonth()
        return _reminders.value.filter { it.date in startOfMonth..endOfMonth }
    }



    private fun getHolidaysFromDatabase(countryCode: String, year: Int): Map<LocalDate, List<HolidayEntity>> {
        val startDate = "${year}-01-01"
        val endDate = "${year}-12-31"
        val holidaysList = holidayDao.getHolidaysForYear(countryCode, startDate, endDate)
        return holidaysList.groupBy { LocalDate.parse(it.date) }
    }





    private fun saveHolidaysToDatabase(holidayMap: Map<LocalDate, List<HolidayEntity>>) {
        holidayDao.insertHolidays(holidayMap.values.flatten())
    }






    fun getHolidaysForMonth(yearMonth: YearMonth): List<HolidayEntity> {
        val startOfMonth = yearMonth.atDay(1)
        val endOfMonth = yearMonth.atEndOfMonth()
        return _holidays.value.filterKeys { it in startOfMonth..endOfMonth }
            .flatMap { it.value }
    }


    fun fetchReminders() {
        viewModelScope.launch {
            reminderViewModel.reminders.collect { remindersList ->
                _reminders.value = remindersList
                remindersList.forEach { reminder ->
                    if (reminder.date.isAfter(LocalDate.now()) || reminder.date.isEqual(LocalDate.now())) {
                        val description = reminder.description ?: "No description provided"
                        scheduleNotification(reminder.date, reminder.title, description)
                    }
                }
            }
        }
    }




    private fun scheduleNotification(date: LocalDate, title: String, message: String, isHoliday: Boolean = false) {


        if (scheduledNotifications.contains(date to title)) {
            Log.d("CalenderViewModel", "Notification already scheduled for ${date}: $title")
            return
        }


        Log.d("CalenderViewModel", "Scheduling notification for ${date}: $title - $message (Holiday: $isHoliday)")
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("NOTIFICATION_ID", date.hashCode())
            putExtra("NOTIFICATION_TITLE", title)
            putExtra("NOTIFICATION_MESSAGE", message)
            putExtra("IS_HOLIDAY", isHoliday)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            date.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val calendar = Calendar.getInstance().apply {
            set(date.year, date.monthValue - 1, date.dayOfMonth, 0, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }

        Log.d("CalenderViewModel", "Scheduling notification for ${date}: $title - $message")
        Log.d("CalenderViewModel", "Calendar time for notification: ${calendar.time}")


        if (calendar.timeInMillis < System.currentTimeMillis()) {

            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
        scheduledNotifications.add(date to title)
    }

}
