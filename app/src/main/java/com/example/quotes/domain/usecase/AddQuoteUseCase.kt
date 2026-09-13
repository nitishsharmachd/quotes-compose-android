package com.example.quotes.domain.usecase

import com.example.quotes.domain.model.Quote
import com.example.quotes.domain.repository.QuoteRepository
import javax.inject.Inject

class AddQuoteUseCase @Inject constructor(
    private val repository: QuoteRepository
) {
    suspend operator fun invoke(quote: Quote): Long {
        require(quote.text.isNotBlank()) { "Quote text cannot be empty" }
        require(quote.author.isNotBlank()) { "Author name cannot be empty" }
        return repository.addQuote(quote)
    }
}
