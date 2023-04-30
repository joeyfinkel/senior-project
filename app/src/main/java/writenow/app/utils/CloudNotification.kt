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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import writenow.app.dbtables.DBUtils
import writenow.app.state.UserState
import java.util.concurrent.TimeUnit

class CloudNotification : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("Token", token)
        FirebaseMessaging.getInstance().subscribeToTopic("all")
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
        }
        createNotification(this, title ?: "", message ?: "")
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun sendNotification(context: Context) {
        val workManager = WorkManager.getInstance(context)
        val constraints = Constraints.Builder().setRequiresBatteryNotLow(true).build()
        val notificationWorker =
            OneTimeWorkRequestBuilder<NotificationWorker>().setInitialDelay(10, TimeUnit.SECONDS)
                .setConstraints(constraints).build()

        workManager.enqueue(notificationWorker)
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            it.result?.let { token ->
                UserState.token = token

                FirebaseMessaging.getInstance().subscribeToTopic("all")
                CoroutineScope(Dispatchers.IO).launch {
                    val json = JSONObject().apply {
                        put("token", token)
                        put("title", "Test")
                        put("body", "This is a test message")
                    }
                    val db =
                        DBUtils(requestUrl = "https://us-central1-writenow-cc43f.cloudfunctions.net/scheduleMessage")

                    db.post(json) {
                        Log.d("TAG", "Message sent")
                    }
                }
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

