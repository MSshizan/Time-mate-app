package com.example.clock.domainLayer

import com.example.clock.domainLayer.entity.ReminderEntity
import com.example.clock.domainLayer.entity.HolidayEntity

sealed class Event {
    data class Holiday(val holiday: HolidayEntity) : Event()
    data class Reminder(val reminder: ReminderEntity) : Event()
}
