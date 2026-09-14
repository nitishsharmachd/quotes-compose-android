package com.example.quotes.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

object QuoteNotificationScheduler {

    fun scheduleQuoteNotifications(context: Context) {
        val workManager = WorkManager.getInstance(context)

        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true) // Prevents running when battery is critically low (<15%)
            .build()

        // Periodic work scheduled every 15 minutes (WorkManager's minimum allowed periodic interval)
        val periodicWorkRequest = PeriodicWorkRequestBuilder<QuoteNotificationWorker>(
            15, TimeUnit.MINUTES
        ).setConstraints(constraints)
            .build()

        workManager.enqueueUniquePeriodicWork(
            QuoteNotificationWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWorkRequest
        )


/*        // Initial one-time work request after 10 seconds for testing/immediate trigger
        val initialWorkRequest = OneTimeWorkRequestBuilder<QuoteNotificationWorker>()
            .setInitialDelay(10, TimeUnit.SECONDS)
            .build()

        workManager.enqueue(initialWorkRequest)*/

    }
}
