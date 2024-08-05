package com.example.clock.domainLayer.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.clock.holidatabase.ConvertorCal
import java.time.LocalDate
import java.time.LocalTime

@Entity(tableName = "reminders")
data class ReminderEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String?,
    @TypeConverters(ConvertorCal::class) val date: LocalDate,
    @TypeConverters(ConvertorCal::class) val time: LocalTime,
    val isOn : Boolean
)

