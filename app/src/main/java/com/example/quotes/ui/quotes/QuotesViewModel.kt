package com.example.quotes.ui.quotes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quotes.data.remote.api.QuotesApiService
import com.example.quotes.domain.model.Quote
import com.example.quotes.domain.model.QuoteCategory
import com.example.quotes.domain.usecase.DeleteQuoteUseCase
import com.example.quotes.domain.usecase.GetQuoteCountUseCase
import com.example.quotes.domain.usecase.GetQuotesUseCase
import com.example.quotes.domain.usecase.RefreshQuotesUseCase
import com.example.quotes.domain.usecase.SearchQuotesUseCase
import com.example.quotes.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class QuotesUiState(
    val quotes: List<Quote> = emptyList(),
    val selectedCategory: QuoteCategory = QuoteCategory.ALL,
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isLoadingMore: Boolean = false,
    val errorMessage: String? = null,
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class QuotesViewModel @Inject constructor(
    private val getQuotesUseCase: GetQuotesUseCase,
    private val searchQuotesUseCase: SearchQuotesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val deleteQuoteUseCase: DeleteQuoteUseCase,
    private val refreshQuotesUseCase: RefreshQuotesUseCase,
    private val getQuoteCountUseCase: GetQuoteCountUseCase,
) : ViewModel() {

    private val _selectedCategory = MutableStateFlow(QuoteCategory.ALL)
    val selectedCategory: StateFlow<QuoteCategory> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    private val _isLoadingMore = MutableStateFlow(false)
    private val _errorMessage = MutableStateFlow<String?>(null)

    private var currentSkip = 0
    private val pageSize = QuotesApiService.DEFAULT_LIMIT
    private var isEndReached = false

    init {
        viewModelScope.launch {
            val cachedCount = getQuoteCountUseCase()
            if (cachedCount > 0) {
                // Initialize pagination skip from previously cached database records
                currentSkip = cachedCount
            } else {
                refreshQuotes()
            }
        }
    }

    private data class FilterState(
        val category: QuoteCategory,
        val query: String,
        val loadingMore: Boolean,
        val error: String?,
    )

    val uiState: StateFlow<QuotesUiState> = combine(
        _selectedCategory,
        _searchQuery,
        _isLoadingMore,
        _errorMessage,
    ) { category, query, loadingMore, error ->
        FilterState(category, query, loadingMore, error)
    }.flatMapLatest { filterState ->
        val flow = if (filterState.query.isNotBlank()) {
            searchQuotesUseCase(filterState.query)
        } else {
            getQuotesUseCase(filterState.category)
        }
        flow.map { quotesList ->
            QuotesUiState(
                quotes = quotesList,
                selectedCategory = filterState.category,
                searchQuery = filterState.query,
                isLoading = false,
                isLoadingMore = filterState.loadingMore,
                errorMessage = filterState.error,
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = QuotesUiState(isLoading = true),
    )

    fun refreshQuotes() {
        viewModelScope.launch {
            _isRefreshing.value = true
            currentSkip = 0
            isEndReached = false
            val result = refreshQuotesUseCase(limit = pageSize, skip = 0)
            result.onSuccess {
                currentSkip = pageSize
            }
            result.onFailure { exception ->
                _errorMessage.value = exception.message ?: "Failed to load quotes"
            }
            _isRefreshing.value = false
        }
    }

    fun loadNextPage() {
        if (_isLoadingMore.value || isEndReached || _searchQuery.value.isNotBlank()) return
        viewModelScope.launch {
            _isLoadingMore.value = true
            val result = refreshQuotesUseCase(limit = pageSize, skip = currentSkip)
            result.onSuccess {
                currentSkip += pageSize
            }
            result.onFailure { exception ->
                isEndReached = true
                _errorMessage.value = exception.message ?: "Failed to load more quotes"
            }
            _isLoadingMore.value = false
        }
    }

    fun onErrorMessageShown() {
        _errorMessage.value = null
    }

    fun onCategorySelected(category: QuoteCategory) {
        _selectedCategory.value = category
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onFavoriteToggled(quote: Quote) {
        viewModelScope.launch {
            toggleFavoriteUseCase(quote.id, !quote.isFavorite)
        }
    }

    fun onDeleteQuote(quote: Quote) {
        viewModelScope.launch {
            deleteQuoteUseCase(quote)
        }
    }
}
