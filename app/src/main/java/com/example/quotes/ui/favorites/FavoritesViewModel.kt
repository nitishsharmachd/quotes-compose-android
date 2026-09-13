package com.example.quotes.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quotes.domain.model.Quote
import com.example.quotes.domain.usecase.DeleteQuoteUseCase
import com.example.quotes.domain.usecase.GetFavoriteQuotesUseCase
import com.example.quotes.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FavoritesUiState(
    val favoriteQuotes: List<Quote> = emptyList(),
    val isLoading: Boolean = false
)

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    getFavoriteQuotesUseCase: GetFavoriteQuotesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val deleteQuoteUseCase: DeleteQuoteUseCase
) : ViewModel() {

    val uiState: StateFlow<FavoritesUiState> = getFavoriteQuotesUseCase()
        .map { quotes ->
            FavoritesUiState(favoriteQuotes = quotes, isLoading = false)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = FavoritesUiState(isLoading = true)
        )

    fun onRemoveFavorite(quote: Quote) {
        viewModelScope.launch {
            toggleFavoriteUseCase(quote.id, false)
        }
    }

    fun onDeleteQuote(quote: Quote) {
        viewModelScope.launch {
            deleteQuoteUseCase(quote)
        }
    }
}
