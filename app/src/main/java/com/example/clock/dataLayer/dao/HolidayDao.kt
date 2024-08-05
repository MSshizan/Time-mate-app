package com.example.clock.dataLayer.dao


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.clock.domainLayer.entity.HolidayEntity

@Dao
interface HolidayDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun insertHolidays(holidays: List<HolidayEntity>)

    @Query("SELECT * FROM holidays WHERE country = :country AND date BETWEEN :startDate AND :endDate")
        fun getHolidaysForYear(country: String, startDate: String, endDate: String): List<HolidayEntity>

    @Query("SELECT * FROM holidays WHERE country = :country AND date = :date")
    fun getHolidaysForDate(country: String, date: String): List<HolidayEntity>
}
