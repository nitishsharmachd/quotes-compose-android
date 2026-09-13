package com.example.quotes.domain.usecase

import com.example.quotes.data.remote.api.QuotesApiService
import com.example.quotes.domain.repository.QuoteRepository
import javax.inject.Inject

class RefreshQuotesUseCase @Inject constructor(
    private val repository: QuoteRepository,
) {
    suspend operator fun invoke(
        limit: Int = QuotesApiService.DEFAULT_LIMIT,
        skip: Int = 0,
    ): Result<Unit> {
        return repository.refreshQuotes(limit = limit, skip = skip)
    }
}
