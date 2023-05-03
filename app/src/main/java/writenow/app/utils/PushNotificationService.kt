package writenow.app.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import writenow.app.R
import writenow.app.state.UserState

class PushNotificationService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("Token", token)
        FirebaseMessaging.getInstance().subscribeToTopic("all")
    }

    override fun onMessageSent(msgId: String) {
        super.onMessageSent(msgId)
        Log.d("TAG", "Message sent $msgId")
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)


        Log.d("TAG", "From: ${remoteMessage.from}")

        // Extract data payload from the message
        val data = remoteMessage.data
        val title = data["title"]
        val message = data["message"]

        remoteMessage.notification?.let {
            Log.d("TAG", "Message Notification Body: ${it.body}")
            createNotification(this, title ?: "", message ?: "")
        }

    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun createNotification(context: Context, title: String, message: String) {
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(context, "default")
            .setSmallIcon(R.drawable.ic_launcher_foreground).setContentTitle(title)
            .setContentText(message).setPriority(NotificationCompat.PRIORITY_HIGH)

        Log.d("TAG", "From: $title")

        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            if (ActivityCompat.checkSelfPermission(
                    context, Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    context as Activity, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1
                )
                return
            }
            notify(1, builder.build())
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun sendNotification(context: Context) {
        val workManager = WorkManager.getInstance(context)
        val constraints = Constraints.Builder().setRequiresBatteryNotLow(true).build()
        val notificationWorker =
            OneTimeWorkRequestBuilder<NotificationWorker>().setConstraints(constraints).build()

        workManager.enqueue(notificationWorker)
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            it.result?.let { token ->
                UserState.token = token

                Log.d("TAG", "Token: $token")

                FirebaseMessaging.getInstance().subscribeToTopic("new_post")
            }
        }
    }
}

//CoroutineScope(Dispatchers.IO).launch {
//    val json = JSONObject().apply {
//        put("token", token)
//        put("title", "Test")
//        put("body", "This is a test message")
//    }
//    val db =
//        DBUtils(requestUrl = "https://us-central1-writenow-cc43f.cloudfunctions.net/scheduleMessage")
//
//    db.post(json){
//        Log.d("TAG", "Message sent")
//    }
//}

