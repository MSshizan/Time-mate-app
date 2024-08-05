package com.example.clock.dataLayer.Repositories

import android.content.Context
import com.example.clock.domainLayer.alarmdataClass
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AlarmRepository(private val context: Context) {
    private val sharedPreferences =
        context.getSharedPreferences("alarms", Context.MODE_PRIVATE)


    fun saveAlarms(alarms: List<alarmdataClass>) {
        val editor = sharedPreferences.edit()
        val json = Gson().toJson(alarms)
        editor.putString("alarms",json)
        editor.apply()

    }

    fun loadAlarm():List<alarmdataClass>{
        val jason = sharedPreferences.getString("alarms","")
        return if(jason.isNullOrEmpty()){
            emptyList()
        }else{
            Gson().fromJson(jason,object :TypeToken<List<alarmdataClass>>(){}.type)
        }
    }
}