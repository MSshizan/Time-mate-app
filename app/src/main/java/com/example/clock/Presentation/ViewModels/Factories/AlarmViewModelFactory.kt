package com.example.clock.Presentation.ViewModels.Factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.clock.dataLayer.Repositories.AlarmRepository
import com.example.clock.Presentation.ViewModels.AlarmViewModule
import com.example.clock.dataLayer.sheduler.alarmSheduler

class AlarmViewModelFactory(
    private val alarmRepository: AlarmRepository,
    private val alarmScheduler: alarmSheduler
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlarmViewModule::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AlarmViewModule(alarmRepository, alarmScheduler) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}