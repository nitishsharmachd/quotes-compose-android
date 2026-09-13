package com.example.quotes.domain.usecase

import com.example.quotes.domain.repository.QuoteRepository
import javax.inject.Inject

class GetQuoteCountUseCase @Inject constructor(
    private val repository: QuoteRepository,
) {
    suspend operator fun invoke(): Int {
        return repository.getRemoteQuoteCount()
    }
}
