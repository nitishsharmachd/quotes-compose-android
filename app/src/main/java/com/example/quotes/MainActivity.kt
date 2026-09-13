package com.example.quotes

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.quotes.ui.navigation.QuotesMainApp
import com.example.quotes.ui.theme.QuotesAppTheme
import com.example.quotes.ui.theme.ThemeViewModel
import com.example.quotes.util.NotificationHelper
import com.example.quotes.worker.QuoteNotificationScheduler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val themeViewModel: ThemeViewModel by viewModels()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { _ ->
        // Permission callback for Android 13+
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Create Notification Channel for Android 8+ (including Android 10)
        NotificationHelper.createNotificationChannel(this)

        // Android 13+ (API 33+) requires runtime permission for notifications
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        // Schedule WorkManager notification background task
        QuoteNotificationScheduler.scheduleQuoteNotifications(this)

        setContent {
            val themeMode by themeViewModel.themeMode.collectAsStateWithLifecycle()

            QuotesAppTheme(themeMode = themeMode) {
                QuotesMainApp(
                    currentThemeMode = themeMode,
                    onThemeModeSelected = { themeViewModel.setThemeMode(it) }
                )
            }
        }
    }
}
