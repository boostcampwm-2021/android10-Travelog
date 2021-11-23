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
import com.thequietz.travelog.util.dateToString
import com.thequietz.travelog.util.stringToDate
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.internal.managers.BroadcastReceiverComponentManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {
    companion object {
        const val TAG = "AlarmReceiver"
        const val NOTIFICATION_ID = 0
        const val CHANNEL_ID = "Travelog"
    }

    private lateinit var notificationManager: NotificationManager
    @Inject
    lateinit var repository: ScheduleRepository

    override fun onReceive(context: Context, intent: Intent) {

        val injector =
            BroadcastReceiverComponentManager.generatedComponent(context) as AlarmReceiver_GeneratedInjector
        injector.injectAlarmReceiver(this)

        Log.e(TAG, "onreceive")
        notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        CoroutineScope(IO).launch {
            val dateList = repository.loadScheduleDateList()
            if (!isValidDate(dateList)) return@launch

            createNotificationChannel()
            deliverNotification(context, intent.extras?.getString("content"), NOTIFICATION_ID)
        }
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

    private fun isValidDate(list: List<String>): Boolean {
        val todayDate = Date(System.currentTimeMillis())
        val today = stringToDate(dateToString(todayDate))
        var dateList: List<String>
        var startDate: Date?
        var endDate: Date?
        for (term in list) {
            dateList = term.split("~")
            startDate = stringToDate(dateList[0])
            endDate = stringToDate(dateList[1])
            if (startDate == null || endDate == null) continue
            else if (startDate.equals(today) || endDate.equals(today)) return true
            else if (startDate.before(today) && endDate.after(today)) return true
        }
        return false
    }
}
