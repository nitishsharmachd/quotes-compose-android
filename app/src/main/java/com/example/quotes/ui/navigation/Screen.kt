package com.example.quotes.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector? = null
) {
    object Quotes : Screen("quotes", "Quotes", Icons.Default.FormatQuote)
    object Favorites : Screen("favorites", "Favorites", Icons.Default.Favorite)
    object AddQuote : Screen("add_quote", "Add", Icons.Default.Add)
    object Detail : Screen("detail/{quoteId}", "Quote Detail") {
        fun createRoute(quoteId: Long) = "detail/$quoteId"
    }

    companion object {
        val bottomNavItems = listOf(Quotes, Favorites, AddQuote)
    }
}
