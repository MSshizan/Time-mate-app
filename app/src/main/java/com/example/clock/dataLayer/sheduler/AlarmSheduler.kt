package com.example.clock.dataLayer.sheduler

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.util.Log
import com.example.clock.dataLayer.receiver.AlarmReceiver
import com.example.clock.domainLayer.alarmdataClass

class alarmSheduler(private val context: Context) {
    private val alarmManger = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager


    fun setAlarm(alarm: alarmdataClass) {
        val now = Calendar.getInstance()
        alarm.selectedDays.forEachIndexed { index, day ->
            val calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, convertTo24HourFormat(alarm.hour, alarm.amPm))
                set(Calendar.MINUTE, alarm.minute)
                set(Calendar.SECOND, 0)
                set(Calendar.DAY_OF_WEEK, getDayOfWeek(day))
                if (before(now)) {
                    add(Calendar.WEEK_OF_YEAR, 1)
                }
            }

            val intent = Intent(context, AlarmReceiver::class.java).apply {
                putExtra("ALARM_ID", alarm.id + index) // Unique ID for each day
                putExtra("MUSIC_URI", alarm.musicUri.toString())
            }
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                alarm.id + index, // Unique ID for each day
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            alarmManger.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent
            )
            Log.d("AlarmScheduler", "Setting alarm with ID: ${alarm.id + index} at ${calendar.time}")
        }
    }


    fun cancelAlarm(alarm: alarmdataClass) {
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarm.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManger.cancel(pendingIntent)
        Log.d("AlarmScheduler", "Cancelled alarm with ID: ${alarm.id}")
    }

    private fun convertTo24HourFormat(hour: Int, amPm: String): Int {
        return when (amPm) {
            "AM" -> if (hour == 12) 0 else hour
            "PM" -> if (hour == 12) 12 else hour + 12
            else -> hour // This should not happen
        }
    }

    private fun getDayOfWeek(day: String): Int {
        return when (day) {
            "Monday" -> Calendar.MONDAY
            "Tuesday" -> Calendar.TUESDAY
            "Wednesday" -> Calendar.WEDNESDAY
            "Thursday" -> Calendar.THURSDAY
            "Friday" -> Calendar.FRIDAY
            "Saturday" -> Calendar.SATURDAY
            "Sunday" -> Calendar.SUNDAY
            else -> Calendar.MONDAY
        }
    }

}
