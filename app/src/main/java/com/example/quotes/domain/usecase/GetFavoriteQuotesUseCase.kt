package com.example.quotes.domain.usecase

import com.example.quotes.domain.model.Quote
import com.example.quotes.domain.repository.QuoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteQuotesUseCase @Inject constructor(
    private val repository: QuoteRepository
) {
    operator fun invoke(): Flow<List<Quote>> {
        return repository.getFavoriteQuotes()
    }
}
