package com.example.clock.domainLayer

data class alarmdataClass(
    val id: Int,
    val hour: Int,
    val minute: Int,
    val amPm: String,
    val label: String,
    val isEnabled: Boolean,
    val musicUri: String? = null,
    val selectedDays: List<String> = emptyList()
)