package com.example.clock.Presentation

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatDelegate
import com.example.clock.ui.theme.ClockTheme
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.clock.dataLayer.Repositories.AlarmRepository
import com.example.clock.domainLayer.CityTimeZone
import com.example.clock.Database.DataBaseApplicationClass.Companion.database
import com.example.clock.Database.DatabaseProvider
import com.example.clock.Presentation.ViewModels.AlarmViewModule
import com.example.clock.Presentation.ViewModels.CalenderViewModel
import com.example.clock.Presentation.ViewModels.ClockViewModule
import com.example.clock.Presentation.ViewModels.Factories.AlarmViewModelFactory
import com.example.clock.Presentation.ViewModels.Factories.CalendarViewModelFactory
import com.example.clock.Presentation.ViewModels.Factories.ReminderViewModelFactory
import com.example.clock.Presentation.ViewModels.ReminderViewModel
import com.example.clock.R
import com.example.clock.dataLayer.Repositories.ReminderRepository
import com.example.clock.dataLayer.sheduler.alarmSheduler
import com.example.clock.api.HolidayApiService
import com.example.clock.holidatabase.AppDatabase
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    private val STORAGE_PERMISSION_CODE = 101
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        enableEdgeToEdge()
        DatabaseProvider.initialize(this)
        val alarmRepository = AlarmRepository(applicationContext)
        val repository = ReminderRepository(database.reminderDao())
        val alarmScheduler = alarmSheduler(applicationContext)
        val database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "holiday_database"
        ).build()
        val holidayDao = database.holidayDao()
        val apiService = Retrofit.Builder()
            .baseUrl("https://date.nager.at/api/v3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HolidayApiService::class.java)
        val alarmViewModelFactory = AlarmViewModelFactory(alarmRepository, alarmScheduler)
        val selectedCities = mutableListOf<CityTimeZone>()
        val reminderfactory = ReminderViewModelFactory(repository)
        val viewModelReminder = ViewModelProvider(this, reminderfactory).get(ReminderViewModel::class.java)
        val factory = CalendarViewModelFactory(context = this,apiService,holidayDao,viewModelReminder)

        checkAndRequestNotificationPermission()

        setContent {
            ClockThemes {

                val viewModel: ClockViewModule =
                    ViewModelProvider(this).get(ClockViewModule::class.java)
                val alarmViewModel: AlarmViewModule =
                    ViewModelProvider(this, alarmViewModelFactory).get(AlarmViewModule::class.java)

                val calenderViewModel: CalenderViewModel =
                    ViewModelProvider(this, factory).get(CalenderViewModel::class.java)

                val viewModelReminder: ReminderViewModel =
                    ViewModelProvider(this, reminderfactory).get(ReminderViewModel::class.java)


                navigation(
                    selectedCities = selectedCities,
                    viewModule = viewModel,
                    alarmViewModule = alarmViewModel,
                    calenderViewModel = calenderViewModel,
                    viewModuleReminder = viewModelReminder
                )
            }
        }
    }

    private fun checkAndRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!NotificationManagerCompat.from(this).areNotificationsEnabled()) {

                val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                    putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
                }
                startActivity(intent)
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            STORAGE_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.d("Permissions", "Storage permission granted")

                } else {

                    Log.d("Permissions", "Storage permission denied")

                    showStoragePermissionNotification()
                }
            }
        }
    }


    private fun showStoragePermissionNotification() {
        try {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = android.net.Uri.fromParts("package", packageName, null)
            }
            val pendingIntent: android.app.PendingIntent = android.app.PendingIntent.getActivity(
                this,
                0,
                intent,
                android.app.PendingIntent.FLAG_UPDATE_CURRENT or android.app.PendingIntent.FLAG_IMMUTABLE
            )

            val builder = NotificationCompat.Builder(this, "storage_permission_channel")
                .setSmallIcon(R.drawable.baseline_notifications_24)
                .setContentTitle("Storage Permission Required")
                .setContentText("Enable storage permission to set alarm songs.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

            with(NotificationManagerCompat.from(this)) {
                notify(102, builder.build())
            }
        } catch (e: SecurityException) {
            Log.e("Permissions", "SecurityException: ${e.message}")

        }
    }
}






