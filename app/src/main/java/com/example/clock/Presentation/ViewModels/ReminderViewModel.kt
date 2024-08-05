package com.example.clock.Presentation.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clock.domainLayer.Reminder
import com.example.clock.domainLayer.entity.ReminderEntity
import com.example.clock.dataLayer.Repositories.ReminderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch



class ReminderViewModel( private val repository: ReminderRepository) : ViewModel() {
    val reminders: StateFlow<List<Reminder>> = repository.reminders
        .map { entities -> entities.map { it.toReminder() } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addReminder(reminder: Reminder) {
        viewModelScope.launch (Dispatchers.IO){
            repository.insertReminder(reminder.toReminderEntity())
        }
    }

    fun updateReminder(reminder: Reminder) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateReminder(reminder.toReminderEntity())
        }
    }

    fun deleteReminder(reminder: Reminder) {
        viewModelScope.launch (Dispatchers.IO){
            repository.deleteReminder(reminder.toReminderEntity())
        }
    }

    private fun ReminderEntity.toReminder(): Reminder {
        return Reminder(id, title, description, date, time, isOn)
    }

    private fun Reminder.toReminderEntity(): ReminderEntity {
        return ReminderEntity(id, title, description, date, time, isOn)
    }
}