package com.example.quotes.ui.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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
import com.example.quotes.util.NetworkConnectivityObserver

@Composable
fun QuotesMainApp(
    currentThemeMode: ThemeMode,
    onThemeModeSelected: (ThemeMode) -> Unit,
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val quotesViewModel: QuotesViewModel = hiltViewModel()

    val context = LocalContext.current
    val connectivityObserver = remember(context) { NetworkConnectivityObserver(context) }
    val isOnline by connectivityObserver.isOnline.collectAsStateWithLifecycle(
        initialValue = connectivityObserver.isCurrentlyConnected()
    )

    val scaffoldState = rememberScaffoldState()
    var isInitialCheck by remember { mutableStateOf(true) }

    LaunchedEffect(isOnline) {
        if (isInitialCheck) {
            isInitialCheck = false
            if (!isOnline) {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = "Working offline - Network unavailable",
                    duration = SnackbarDuration.Indefinite
                )
            }
        } else {
            scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
            if (!isOnline) {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = "Working offline - Network connection lost",
                    duration = SnackbarDuration.Indefinite
                )
            } else {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = "Back online - Network connected",
                    duration = SnackbarDuration.Short
                )
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.systemBarsPadding(),
        snackbarHost = { hostState ->
            SnackbarHost(
                hostState = hostState,
                snackbar = { snackbarData ->
                    val isOfflineMsg = snackbarData.message.contains("offline", ignoreCase = true)
                    val bgColor = if (isOfflineMsg) Color(0xFFD32F2F) else Color(0xFF2E7D32)
                    Snackbar(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        backgroundColor = bgColor,
                        contentColor = Color.White,
                        shape = RoundedCornerShape(8.dp),
                        elevation = 6.dp
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = if (isOfflineMsg) Icons.Default.WifiOff else Icons.Default.Wifi,
                                contentDescription = null,
                                tint = Color.White
                            )
                            Text(
                                text = snackbarData.message,
                                style = MaterialTheme.typography.body2.copy(
                                    fontWeight = FontWeight.Medium,
                                    color = Color.White
                                )
                            )
                        }
                    }
                }
            )
        },
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
