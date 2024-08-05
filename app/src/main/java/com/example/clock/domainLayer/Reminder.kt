package com.example.clock.domainLayer

import java.time.LocalDate
import java.time.LocalTime

data class Reminder(
    val id : Int,
    val title: String,
    val description: String?,
    val date: LocalDate,
    val time : LocalTime,
    val isOn : Boolean
)
