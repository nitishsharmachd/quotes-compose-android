package com.example.quotes.domain.usecase

import com.example.quotes.domain.repository.QuoteRepository
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val repository: QuoteRepository
) {
    suspend operator fun invoke(id: Long, isFavorite: Boolean) {
        repository.toggleFavorite(id, isFavorite)
    }
}
