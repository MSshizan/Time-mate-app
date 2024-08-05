package com.example.clock.dataLayer.Repositories


import com.example.clock.dataLayer.dao.ReminderDao
import com.example.clock.domainLayer.entity.ReminderEntity
import kotlinx.coroutines.flow.Flow

class ReminderRepository(private val reminderDao: ReminderDao) {
    val reminders: Flow<List<ReminderEntity>> = reminderDao.getReminders()

    fun insertReminder(reminder: ReminderEntity) {
        reminderDao.insertReminder(reminder)
    }

   fun updateReminder(reminder: ReminderEntity) {
        reminderDao.updateReminder(reminder)
    }

     fun deleteReminder(reminder: ReminderEntity) {
        reminderDao.deleteReminder(reminder)
    }
}