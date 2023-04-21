package writenow.app.utils

import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.concurrent.TimeUnit

class CloudNotification : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        FirebaseMessaging.getInstance().subscribeToTopic("all")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Extract data payload from the message
        val data = remoteMessage.data
        val title = data["title"]
        val message = data["message"]

        // Create a notification using the payload data
        val notificationBuilder = NotificationCompat.Builder(this, "default").setContentTitle(title)
            .setContentText(message).setPriority(NotificationCompat.PRIORITY_DEFAULT)

        // Show the notification
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notificationBuilder.build())
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun sendNotification(context: Context) {
        val workManager = WorkManager.getInstance(context)
        val constraints = Constraints.Builder().setRequiresBatteryNotLow(true).build()
        val notificationWorker =
            OneTimeWorkRequestBuilder<NotificationWorker>().setInitialDelay(10, TimeUnit.SECONDS)
                .setConstraints(constraints).build()

        workManager.enqueue(notificationWorker)

        workManager.getWorkInfoByIdLiveData(notificationWorker.id).observeForever {
            Log.d("NotificationWorker", "sendNotification: ${it.state}")

            if (it.state == WorkInfo.State.SUCCEEDED) {
                createNotification(context, "Write Now", "Write Now is waiting for you!")
                Log.d("NotificationWorker", "sendNotification: SUCCEEDED")
            }

        }
    }

}

