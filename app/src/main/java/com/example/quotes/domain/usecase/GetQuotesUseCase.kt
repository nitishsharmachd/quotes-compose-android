package com.example.quotes.domain.usecase

import com.example.quotes.domain.model.Quote
import com.example.quotes.domain.model.QuoteCategory
import com.example.quotes.domain.repository.QuoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetQuotesUseCase @Inject constructor(
    private val repository: QuoteRepository
) {
    operator fun invoke(category: QuoteCategory = QuoteCategory.ALL): Flow<List<Quote>> {
        return repository.getQuotesByCategory(category)
    }
}
