package com.example.clock.Presentation.ViewModels.Factories

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.clock.Presentation.ViewModels.CalenderViewModel
import com.example.clock.Presentation.ViewModels.ReminderViewModel
import com.example.clock.api.HolidayApiService
import com.example.clock.dataLayer.dao.HolidayDao

class CalendarViewModelFactory(
    private val context: Context,
    private val apiService: HolidayApiService,
    private val holidayDao: HolidayDao,
    private val reminderViewModel: ReminderViewModel


) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(CalenderViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CalenderViewModel(apiService,holidayDao,reminderViewModel,context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}