package com.example.clock.dataLayer.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.clock.domainLayer.CityTimeZone
import kotlinx.coroutines.flow.Flow

@Dao
interface cityTimeZoneDao {

    @Query("SElECT * FROM CityTimeZone")
   fun getAllSelectedCities(): Flow<List<CityTimeZone>>

   @Insert
   fun addCity(cityTimeZone: CityTimeZone)

    @Query("DELETE FROM CityTimeZone WHERE city = :city")
    fun deleteCity(city: String)

    @Query("SELECT * FROM CityTimeZone WHERE city = :cityName")
        fun getCityByName(cityName: String): CityTimeZone?
}