package com.example.quotes.data.mapper

import com.example.quotes.data.local.entity.QuoteEntity
import com.example.quotes.data.remote.model.QuoteDto
import com.example.quotes.domain.model.Quote
import com.example.quotes.domain.model.QuoteCategory

fun QuoteEntity.toDomainModel(): Quote {
    val categoryEnum = try {
        QuoteCategory.valueOf(category.uppercase())
    } catch (e: Exception) {
        QuoteCategory.MOTIVATIONAL
    }
    return Quote(
        id = id,
        text = text,
        author = author,
        category = categoryEnum,
        isFavorite = isFavorite,
        isCustom = isCustom,
        createdAt = createdAt
    )
}

fun Quote.toEntity(): QuoteEntity {
    return QuoteEntity(
        id = id,
        text = text,
        author = author,
        category = category.name,
        isFavorite = isFavorite,
        isCustom = isCustom,
        createdAt = createdAt
    )
}

fun QuoteDto.toEntity(): QuoteEntity {
    return QuoteEntity(
        id = id,
        text = quote,
        author = author,
        category = "MOTIVATIONAL",
        isFavorite = false,
        isCustom = false,
        createdAt = System.currentTimeMillis()
    )
}
