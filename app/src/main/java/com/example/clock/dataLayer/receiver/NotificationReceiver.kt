package com.example.clock.dataLayer.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.clock.R

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val notificationId = intent.getIntExtra("NOTIFICATION_ID", -1)
        val notificationTitle = intent.getStringExtra("NOTIFICATION_TITLE") ?: "Reminder"
        val notificationMessage = intent.getStringExtra("NOTIFICATION_MESSAGE") ?: "No message"
        val isHoliday = intent.getBooleanExtra("IS_HOLIDAY", false)

        Log.d("NotificationReceiver", "Received notification intent: $intent")
        Log.d("NotificationReceiver", "Notification ID: $notificationId, Title: $notificationTitle, Message: $notificationMessage")

        if (notificationId != -1) {
            if (hasNotificationPermission(context)) {
                showNotification(context, notificationId, notificationTitle, notificationMessage,isHoliday)

            }else{
                Log.e("NotificationReceiver", "Notification permission not granted or invalid ID")
            }
        }else{
            Log.e("NotificationReceiver", "Invalid notification ID")
        }
    }

    private fun showNotification(
        context: Context,
        notificationId: Int,
        notificationTitle: String,
        notificationMessage: String,
        isHoliday: Boolean = false
    ) {
        val channelId = if (isHoliday) "HOLIDAY_CHANNEL_ID" else "REMINDER_CHANNEL_ID"
        createNotificationChannel(context, channelId, isHoliday)

        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.baseline_notifications_24)
            .setContentTitle(notificationTitle)
            .setContentText(notificationMessage)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        try {
            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.notify(notificationId, notificationBuilder.build())
        } catch (e: SecurityException) {

        }
    }

    private fun createNotificationChannel(
        context: Context,
        channelId: String,
        isHoliday: Boolean
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = if (isHoliday) "Holiday Notifications" else "Reminder Notifications"
            val descriptionText = if (isHoliday) "Notifications for holidays" else "Notifications for reminders"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
            Log.d("NotificationReceiver", "Notification channel created: $channelId")
        }
    }

    private fun hasNotificationPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.areNotificationsEnabled()
        } else {
            true
        }
    }
}