package com.example.quotes.domain.usecase

import com.example.quotes.domain.model.Quote
import com.example.quotes.domain.repository.QuoteRepository
import javax.inject.Inject

class DeleteQuoteUseCase @Inject constructor(
    private val repository: QuoteRepository
) {
    suspend operator fun invoke(quote: Quote) {
        repository.deleteQuote(quote)
    }
}
