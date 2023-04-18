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
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit

class NotificationWorker(context: Context, userParams: WorkerParameters) :
    Worker(context, userParams) {
    override fun doWork(): Result {
        return try {
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
                context as Activity, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1
            )
            return
        }
        notify(1, builder.build())
    }
}

private fun generateRandomTime(): LocalTime {
    val random = Random()
    val hour = random.nextInt(12) + 1
    val minute = random.nextInt(60)
    val amPm = if (random.nextBoolean()) "AM" else "PM"

    return LocalTime.of(hour, minute).withHour(hour + if (amPm == "PM") 12 else 0)
}

private fun generateRandomTimeRange(): Pair<String, String> {
    val startTime = generateRandomTime()
    val endTime = generateRandomTime()
    val format = DateTimeFormatter.ofPattern("h:mm a")

    return if (endTime.isBefore(startTime)) Pair(
        format.format(endTime), format.format(startTime)
    ) else Pair(format.format(startTime), format.format(endTime))
}

private fun getTimeInMillis(formattedTime: String, randomTime: String): Long {
    val formatter = DateTimeFormatter.ofPattern("h:mm a")
    val time = LocalTime.parse(formattedTime.ifEmpty { randomTime }, formatter)

    return time.atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun sendNotification(context: Context, notificationToSend: (Context) -> Unit) {
    val workManager = WorkManager.getInstance(context)
    val constraints = Constraints.Builder().setRequiresBatteryNotLow(true).build()
    val notificationWorker =
        OneTimeWorkRequestBuilder<NotificationWorker>().setInitialDelay(10, TimeUnit.SECONDS)
            .setConstraints(constraints).build()

    workManager.enqueue(notificationWorker)

    workManager.getWorkInfoByIdLiveData(notificationWorker.id).observeForever {
        Log.d("NotificationWorker", "sendNotification: ${it.state}")

        if (it.state == WorkInfo.State.SUCCEEDED) {
            notificationToSend(context)
            Log.d("NotificationWorker", "sendNotification: SUCCEEDED")
        }

    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun sendNotification(context: Context, activeHours: ActiveHours, activeDays: Set<String>) {
    val (randomTimeStart, randomTimeEnd) = generateRandomTimeRange()
    val currentTime = Calendar.getInstance()
    val currentDayOfWeek = LocalDate.now().dayOfWeek.name
    val start = Calendar.getInstance().apply {
        timeInMillis = getTimeInMillis(activeHours.start, randomTimeStart)
    }
    val end = Calendar.getInstance().apply {
        timeInMillis = getTimeInMillis(activeHours.end, randomTimeEnd)
    }

    if (currentTime in start..end && activeDays.any {
            it.equals(
                currentDayOfWeek, ignoreCase = true
            )
        }) {
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
}