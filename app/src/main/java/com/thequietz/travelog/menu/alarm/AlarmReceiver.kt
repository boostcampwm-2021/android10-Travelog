package com.thequietz.travelog.menu.alarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.thequietz.travelog.MainActivity
import com.thequietz.travelog.R
import com.thequietz.travelog.schedule.repository.ScheduleRepository
import java.util.Date
import javax.inject.Inject

class AlarmReceiver @Inject internal constructor(
    val repository: ScheduleRepository,
) : BroadcastReceiver() {
    companion object {
        const val TAG = "AlarmReceiver"
        const val NOTIFICATION_ID = 0
        const val CHANNEL_ID = "Travelog"
    }

    lateinit var notificationManager: NotificationManager

    override fun onReceive(context: Context, intent: Intent) {
        Log.e(TAG, "onreceive")
        notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val dateList = repository.loadScheduleDateList()
        createNotificationChannel()
        deliverNotification(context, intent.extras?.getString("content"), NOTIFICATION_ID)
    }

    private fun deliverNotification(context: Context, content: String?, id: Int) {
        val contentIntent = Intent(context, MainActivity::class.java)
        val contentPendingIntent = PendingIntent.getActivity(
            context,
            id,
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_edit)
            .setContentTitle("Travelog")
            .setContentText(content)
            .setContentIntent(contentPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(CHANNEL_ID, "일정 알림", NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = R.color.green_travelog
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Travelog 일정 알림"
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun isValidDate(list: List<String>) {
        val today = Date(System.currentTimeMillis())
    }
}
