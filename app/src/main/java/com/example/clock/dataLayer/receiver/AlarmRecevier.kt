package com.example.clock.dataLayer.receiver

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.clock.R


class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val alarmId = intent.getIntExtra("ALARM_ID", -1)
        val alarmName = intent.getStringExtra("ALARM_NAME") ?: "Alarm"
        val alarmTime = intent.getStringExtra("ALARM_TIME") ?: "Time"
        val musicUri = intent.getStringExtra("MUSIC_URI")

        if (alarmId != -1) {
            if (hasNotificationPermission(context)) {
                showAlarmNotification(context, alarmId, alarmName, alarmTime)
            }
            AlarmRingService.playAlarmRingtone(context, musicUri)
        }
    }

    private fun showAlarmNotification(
        context: Context,
        alarmId: Int,
        alarmName: String,
        alarmTime: String
    ) {
        val channelId = "ALARM_CHANNEL_$alarmId"
        createNotificationChannel(context, channelId)

        val stopAlarmIntent = getStopAlarmPendingIntent(context, alarmId)

        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.baseline_alarm_24)
            .setContentTitle(alarmName)
            .setContentText("Alarm set for $alarmTime")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .addAction(R.drawable.baseline_stop_circle_24, "Stop", stopAlarmIntent)
            .setOngoing(true)

        try {
            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.notify(alarmId, notificationBuilder.build())
        } catch (e: SecurityException) {

        }
    }

    private fun getStopAlarmPendingIntent(context: Context, alarmId: Int): PendingIntent {
        val stopAlarmIntent = Intent(context, StopAlarmReceiver::class.java).apply {
            putExtra("ALARM_ID", alarmId)
        }
        return PendingIntent.getBroadcast(
            context,
            alarmId,
            stopAlarmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun createNotificationChannel(context: Context, channelId: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Alarm Channel"
            val descriptionText = "Channel for Alarm notifications"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


    private fun hasNotificationPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    }
}

object AlarmRingService {
    var mediaPlayer: MediaPlayer? = null

    fun playAlarmRingtone(context: Context, musicUri: String?) {
        val uri =
            if (!musicUri.isNullOrEmpty()) Uri.parse(musicUri) else getDefaultRingtoneUri(context)
        mediaPlayer = MediaPlayer().apply {
            setDataSource(context, uri)
            setAudioStreamType(AudioManager.STREAM_ALARM)
            isLooping = true
            prepare()
            start()
        }
        // Optionally stop after a certain period
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            stopAlarm()
        }, 180000) // Stop after 180 seconds
    }

    fun stopAlarm() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun getDefaultRingtoneUri(context: Context): Uri {
        return RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
    }
}