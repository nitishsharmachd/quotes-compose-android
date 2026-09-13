package com.example.quotes.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.quotes.ui.theme.ThemeMode
import com.example.quotes.util.NotificationHelper

@Composable
fun ThemeOptionsMenu(
    currentThemeMode: ThemeMode,
    onThemeModeSelected: (ThemeMode) -> Unit
) {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }

    IconButton(onClick = { expanded = true }) {
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = "Settings / Theme",
            tint = MaterialTheme.colors.onPrimary
        )
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        DropdownMenuItem(enabled = false, onClick = {}) {
            Text(
                text = "Choose Theme",
                style = MaterialTheme.typography.subtitle2,
                color = MaterialTheme.colors.primary
            )
        }

        ThemeMode.values().forEach { mode ->
            DropdownMenuItem(
                onClick = {
                    onThemeModeSelected(mode)
                    expanded = false
                }
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = currentThemeMode == mode,
                        onClick = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = mode.displayName)
                }
            }
        }

        Divider()

        DropdownMenuItem(
            onClick = {
                NotificationHelper.showQuoteNotification(
                    context,
                    "The only way to do great work is to love what you do.",
                    "Steve Jobs"
                )
                expanded = false
            }
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Test Notification Now")
            }
        }
    }
}
