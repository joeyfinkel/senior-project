package writenow.app.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import writenow.app.R

class HomeScreenReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("HomeScreenReceiver", "onReceive: $intent")
        if (intent.action == Intent.ACTION_CLOSE_SYSTEM_DIALOGS) {
            Log.d("HomeScreenReceiver", "onReceive: Home button pressed")
            // User has gone to the home screen
            showNotification(context)
        }
    }

    private fun showNotification(context: Context) {
        // Create the notification channel
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "default",
                "Default",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        // Create the notification
        val notification = NotificationCompat.Builder(context, "default")
            .setContentTitle("Title")
            .setContentText("Text")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setAutoCancel(true)
            .build()

        // Show the notification
        notificationManager.notify(0, notification)
    }
}