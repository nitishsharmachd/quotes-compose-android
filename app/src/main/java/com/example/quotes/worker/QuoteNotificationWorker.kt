package com.example.quotes.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.quotes.domain.repository.QuoteRepository
import com.example.quotes.util.NotificationHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.firstOrNull

@HiltWorker
class QuoteNotificationWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: QuoteRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val quotes = repository.getAllQuotes().firstOrNull()
            val randomQuote = quotes?.randomOrNull()

            if (randomQuote != null) {
                NotificationHelper.showQuoteNotification(context, randomQuote.text, randomQuote.author)
            }
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    companion object {
        const val WORK_NAME = "QuoteNotificationWork"
    }
}
