package com.example.quotes.util

import android.content.BroadcastReceiver
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast

class NotificationActionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        val quoteText = intent.getStringExtra(EXTRA_QUOTE_TEXT) ?: return
        val author = intent.getStringExtra(EXTRA_AUTHOR) ?: return

        when (action) {
            ACTION_COPY -> {
                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("Quote", "“$quoteText” — $author")
                clipboard.setPrimaryClip(clip)
                Toast.makeText(context, "Quote copied to clipboard! 📋", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        const val ACTION_COPY = "com.example.quotes.ACTION_COPY_QUOTE"
        const val EXTRA_QUOTE_TEXT = "extra_quote_text"
        const val EXTRA_AUTHOR = "extra_author"
    }
}
