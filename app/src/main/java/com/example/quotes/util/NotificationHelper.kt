package com.example.quotes.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.quotes.MainActivity
import com.example.quotes.R

object NotificationHelper {

    const val CHANNEL_ID = "quote_notification_channel"
    const val CHANNEL_NAME = "Daily Quote Notifications"

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = "Shows random inspirational quotes periodically"
                enableVibration(true)
            }
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showQuoteNotification(context: Context, quoteText: String, author: String) {
        createNotificationChannel(context)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
        val contentPendingIntent = PendingIntent.getActivity(context, 0, intent, flags)

        // Share Action Intent
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "“$quoteText” — $author")
        }
        val shareChooser = Intent.createChooser(shareIntent, "Share Quote").apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        val sharePendingIntent = PendingIntent.getActivity(
            context,
            1,
            shareChooser,
            flags
        )

        // Copy Action Intent
        val copyIntent = Intent(context, NotificationActionReceiver::class.java).apply {
            action = NotificationActionReceiver.ACTION_COPY
            putExtra(NotificationActionReceiver.EXTRA_QUOTE_TEXT, quoteText)
            putExtra(NotificationActionReceiver.EXTRA_AUTHOR, author)
        }
        val copyPendingIntent = PendingIntent.getBroadcast(
            context,
            2,
            copyIntent,
            flags
        )

        // Collapsed Custom View
        val collapsedViews = RemoteViews(context.packageName, R.layout.notification_quote_collapsed).apply {
            setTextViewText(R.id.notification_quote_text, "“$quoteText” — $author")
        }

        // Expanded Custom View
        val expandedViews = RemoteViews(context.packageName, R.layout.notification_quote_expanded).apply {
            setTextViewText(R.id.notification_quote_text, "“$quoteText”")
            setTextViewText(R.id.notification_author, "— $author")
        }

        val largeIcon = BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setLargeIcon(largeIcon)
            .setColor(0xFF7A33E0.toInt())
            .setContentTitle("Inspiration for You 💡")
            .setContentText("“$quoteText” — $author")
            .setCustomContentView(collapsedViews)
            .setCustomBigContentView(expandedViews)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setContentIntent(contentPendingIntent)
            .setAutoCancel(true)
            .addAction(
                android.R.drawable.ic_menu_share,
                "Share",
                sharePendingIntent
            )
            .addAction(
                android.R.drawable.ic_menu_set_as,
                "Copy",
                copyPendingIntent
            )
            .build()

        try {
            NotificationManagerCompat.from(context).notify(System.currentTimeMillis().toInt(), notification)
        } catch (_: SecurityException) {
            // Notification permission missing or disabled
        }
    }
}
