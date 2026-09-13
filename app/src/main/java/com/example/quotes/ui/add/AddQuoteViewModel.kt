package com.example.quotes.ui.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quotes.domain.model.Quote
import com.example.quotes.domain.model.QuoteCategory
import com.example.quotes.domain.usecase.AddQuoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddQuoteUiState(
    val quoteText: String = "",
    val author: String = "",
    val category: QuoteCategory = QuoteCategory.MOTIVATIONAL,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

sealed interface AddQuoteEvent {
    object QuoteSaved : AddQuoteEvent
}

@HiltViewModel
class AddQuoteViewModel @Inject constructor(
    private val addQuoteUseCase: AddQuoteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddQuoteUiState())
    val uiState: StateFlow<AddQuoteUiState> = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<AddQuoteEvent>()
    val eventFlow: SharedFlow<AddQuoteEvent> = _eventFlow.asSharedFlow()

    fun onQuoteTextChanged(text: String) {
        _uiState.value = _uiState.value.copy(quoteText = text, errorMessage = null)
    }

    fun onAuthorChanged(author: String) {
        _uiState.value = _uiState.value.copy(author = author, errorMessage = null)
    }

    fun onCategorySelected(category: QuoteCategory) {
        _uiState.value = _uiState.value.copy(category = category)
    }

    fun saveQuote() {
        val currentState = _uiState.value
        if (currentState.quoteText.isBlank()) {
            _uiState.value = currentState.copy(errorMessage = "Quote text cannot be empty")
            return
        }
        if (currentState.author.isBlank()) {
            _uiState.value = currentState.copy(errorMessage = "Author name cannot be empty")
            return
        }

        viewModelScope.launch {
            _uiState.value = currentState.copy(isLoading = true, errorMessage = null)
            try {
                val newQuote = Quote(
                    text = currentState.quoteText.trim(),
                    author = currentState.author.trim(),
                    category = currentState.category,
                    isCustom = true
                )
                addQuoteUseCase(newQuote)
                _uiState.value = AddQuoteUiState()
                _eventFlow.emit(AddQuoteEvent.QuoteSaved)
            } catch (e: Exception) {
                _uiState.value = currentState.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Failed to save quote"
                )
            }
        }
    }
}
