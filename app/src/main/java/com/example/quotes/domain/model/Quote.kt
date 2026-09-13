package com.example.quotes.domain.model

data class Quote(
    val id: Long = 0,
    val text: String,
    val author: String,
    val category: QuoteCategory,
    val isFavorite: Boolean = false,
    val isCustom: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
