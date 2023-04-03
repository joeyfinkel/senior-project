package writenow.app.utils

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.*
import writenow.app.R
import java.util.concurrent.TimeUnit

class NotificationWorker(context: Context, userParams: WorkerParameters) :
    Worker(context, userParams) {
    override fun doWork(): Result {
        return try {
            for (i in 0..10) {
                Log.i("NotificationWorker:", "$i")
            }

            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}

fun createNotificationChannel(context: Context) {
    val name = "Notification Channel"
    val descriptionText = "Notification Channel Description"
    val importance = NotificationManager.IMPORTANCE_DEFAULT
    val channel = NotificationChannel("default", name, importance).apply {
        description = descriptionText
    }
    // Register the channel with the system
    val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(channel)
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun testNotification(context: Context) {
    val builder = NotificationCompat.Builder(context, "default")
        .setSmallIcon(R.drawable.ic_launcher_foreground).setContentTitle("Notification Title")
        .setContentText("Notification Content").setPriority(NotificationCompat.PRIORITY_DEFAULT)

    with(NotificationManagerCompat.from(context)) {
        // notificationId is a unique int for each notification that you must define
        if (ActivityCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                1
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
        OneTimeWorkRequestBuilder<NotificationWorker>().setInitialDelay(10, TimeUnit.SECONDS)
            .setConstraints(constraints).build()

    workManager.enqueue(notificationWorker)

    workManager.getWorkInfoByIdLiveData(notificationWorker.id).observeForever {
        Log.d("NotificationWorker", "sendNotification: ${it.state}")

        if (it.state == WorkInfo.State.SUCCEEDED) {
            testNotification(context)
            Log.d("NotificationWorker", "sendNotification: SUCCEEDED")
        }

    }
}