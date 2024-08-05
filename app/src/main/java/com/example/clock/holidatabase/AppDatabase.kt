package com.example.clock.holidatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.clock.dataLayer.dao.ReminderDao
import com.example.clock.dataLayer.dao.HolidayDao
import com.example.clock.domainLayer.entity.ReminderEntity
import com.example.clock.domainLayer.entity.HolidayEntity

@Database(entities = [HolidayEntity::class, ReminderEntity::class], version = 2)
@TypeConverters(ConvertorCal::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun holidayDao(): HolidayDao
    abstract fun reminderDao(): ReminderDao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDataBase(context : Context): AppDatabase {
            return  INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "holiday_database"
                ).addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS reminders (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                            "title TEXT NOT NULL, " +
                            "description TEXT NOT NULL, " +
                            "date TEXT NOT NULL, " +
                            "time TEXT NOT NULL, " +
                            "isOn INTEGER NOT NULL)"
                )
            }
        }
    }
}