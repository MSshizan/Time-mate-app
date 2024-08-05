package com.example.clock.Database
import androidx.room.TypeConverter
import java.time.LocalTime
import java.time.ZoneId

class convertors {
    @TypeConverter
    fun fromZoneId(zoneId: ZoneId): String {
        return zoneId.id
    }

    @TypeConverter
    fun toZoneId(zoneId: String): ZoneId {
        return ZoneId.of(zoneId)
    }

    @TypeConverter
    fun fromLocalTime(localTime: LocalTime): String {
        return localTime.toString()
    }

    @TypeConverter
    fun toLocalTime(localTime: String): LocalTime {
        return LocalTime.parse(localTime)
    }
}
