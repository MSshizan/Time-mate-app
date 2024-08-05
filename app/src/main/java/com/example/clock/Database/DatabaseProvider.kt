package com.example.clock.Database

import android.content.Context
import com.example.clock.holidatabase.AppDatabase

object DatabaseProvider {
    private var appDatabase :AppDatabase? = null

    fun initialize(context: Context){
            if(appDatabase == null){
                appDatabase =AppDatabase.getDataBase(context)
            }
        }
        fun getDatabase(): AppDatabase{
            return appDatabase ?: throw IllegalStateException("DatabaseProvider")
        }
    }

