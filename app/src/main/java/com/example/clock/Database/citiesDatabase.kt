package com.example.clock.Database
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.clock.dataLayer.dao.cityTimeZoneDao
import com.example.clock.domainLayer.CityTimeZone


@Database(entities = [CityTimeZone::class], version = 1)
@TypeConverters(convertors::class)
abstract class citiesDatabase : RoomDatabase(){

    companion object{
        const val NAME = "Cities_DB"
    }

    abstract fun getCitiesdao(): cityTimeZoneDao
}