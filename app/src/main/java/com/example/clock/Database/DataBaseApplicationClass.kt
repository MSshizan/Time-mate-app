package com.example.clock.Database

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.room.Room
import com.example.clock.api.HolidayApiService
import com.example.clock.holidatabase.AppDatabase
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DataBaseApplicationClass: Application() {
    companion object{
        lateinit var CitiesDataBase : citiesDatabase
        lateinit var database: AppDatabase
        lateinit var apiService: HolidayApiService
    }

    override fun onCreate() {
        super.onCreate()
        createExternalStorageNotificationChannel(this)

        DatabaseProvider.initialize(this)


        database = AppDatabase.getDataBase(this)


        apiService = Retrofit.Builder()
            .baseUrl("https://calendarific.com/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HolidayApiService::class.java)


        CitiesDataBase = Room.databaseBuilder(
            applicationContext,
            citiesDatabase::class.java,
            citiesDatabase.NAME

        ).build()
    }
    private fun createExternalStorageNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "External Storage Notifications"
            val descriptionText = "Channel for external storage related notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("external_storage_channel", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)


            Log.d("NotificationChannel", "Notification channel created: $name")
        }
    }
}