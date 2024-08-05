package com.example.clock.domainLayer.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "holidays")
data class HolidayEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val date: String,
    val country: String
)