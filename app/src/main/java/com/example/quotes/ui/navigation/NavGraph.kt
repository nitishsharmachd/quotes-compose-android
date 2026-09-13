package com.example.quotes.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.quotes.ui.add.AddQuoteScreen
import com.example.quotes.ui.add.AddQuoteViewModel
import com.example.quotes.ui.detail.QuoteDetailScreen
import com.example.quotes.ui.favorites.FavoritesScreen
import com.example.quotes.ui.favorites.FavoritesViewModel
import com.example.quotes.ui.quotes.QuotesScreen
import com.example.quotes.ui.quotes.QuotesViewModel
import com.example.quotes.ui.theme.ThemeMode

@Composable
fun QuotesMainApp(
    currentThemeMode: ThemeMode,
    onThemeModeSelected: (ThemeMode) -> Unit,
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val quotesViewModel: QuotesViewModel = hiltViewModel()

    Scaffold(
        modifier = Modifier.systemBarsPadding(),
        bottomBar = {
            if (currentRoute in Screen.bottomNavItems.map { it.route }) {
                BottomNavigation(
                    backgroundColor = MaterialTheme.colors.surface,
                    contentColor = MaterialTheme.colors.primary
                ) {
                    Screen.bottomNavItems.forEach { screen ->
                        BottomNavigationItem(
                            icon = {
                                screen.icon?.let { icon ->
                                    Icon(imageVector = icon, contentDescription = screen.title)
                                }
                            },
                            label = { Text(screen.title) },
                            selected = currentRoute == screen.route,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Quotes.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Quotes.route) {
                QuotesScreen(
                    viewModel = quotesViewModel,
                    currentThemeMode = currentThemeMode,
                    onThemeModeSelected = onThemeModeSelected,
                    onQuoteClick = { quote ->
                        navController.navigate(Screen.Detail.createRoute(quote.id))
                    }
                )
            }

            composable(Screen.Favorites.route) {
                val favoritesViewModel: FavoritesViewModel = hiltViewModel()
                FavoritesScreen(
                    viewModel = favoritesViewModel,
                    currentThemeMode = currentThemeMode,
                    onThemeModeSelected = onThemeModeSelected,
                    onQuoteClick = { quote ->
                        navController.navigate(Screen.Detail.createRoute(quote.id))
                    }
                )
            }

            composable(Screen.AddQuote.route) {
                val addQuoteViewModel: AddQuoteViewModel = hiltViewModel()
                AddQuoteScreen(
                    viewModel = addQuoteViewModel,
                    currentThemeMode = currentThemeMode,
                    onThemeModeSelected = onThemeModeSelected,
                    onQuoteSaved = {
                        navController.navigate(Screen.Quotes.route) {
                            popUpTo(Screen.Quotes.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(
                route = Screen.Detail.route,
                arguments = listOf(navArgument("quoteId") { type = NavType.LongType })
            ) { backStackEntry ->
                val quoteId = backStackEntry.arguments?.getLong("quoteId") ?: 0L
                val quotesUiState by quotesViewModel.uiState.collectAsStateWithLifecycle()
                val selectedQuote = quotesUiState.quotes.find { it.id == quoteId }
                    ?: quotesUiState.quotes.firstOrNull()

                selectedQuote?.let { quote ->
                    QuoteDetailScreen(
                        quote = quote,
                        onFavoriteToggle = { quotesViewModel.onFavoriteToggled(it) },
                        onBackClick = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}
