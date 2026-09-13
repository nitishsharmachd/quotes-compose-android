package com.example.quotes.domain.repository

import com.example.quotes.data.local.dao.QuoteDao
import com.example.quotes.data.mapper.toDomainModel
import com.example.quotes.data.mapper.toEntity
import com.example.quotes.data.remote.api.QuotesApiService
import com.example.quotes.domain.model.Quote
import com.example.quotes.domain.model.QuoteCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuoteRepository @Inject constructor(
    private val quoteDao: QuoteDao,
    private val quotesApiService: QuotesApiService,
) {

   fun getAllQuotes(): Flow<List<Quote>> {
        return quoteDao.getAllQuotes().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    fun getFavoriteQuotes(): Flow<List<Quote>> {
        return quoteDao.getFavoriteQuotes().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    fun getQuotesByCategory(category: QuoteCategory): Flow<List<Quote>> {
        return if (category == QuoteCategory.ALL) {
            getAllQuotes()
        } else {
            quoteDao.getQuotesByCategory(category.name).map { entities ->
                entities.map { it.toDomainModel() }
            }
        }
    }

    fun searchQuotes(query: String): Flow<List<Quote>> {
        return if (query.isBlank()) {
            getAllQuotes()
        } else {
            quoteDao.searchQuotes(query.trim()).map { entities ->
                entities.map { it.toDomainModel() }
            }
        }
    }

    fun getQuoteById(id: Long): Flow<Quote?> {
        return quoteDao.getQuoteById(id).map { entity ->
            entity?.toDomainModel()
        }
    }

    suspend fun getRemoteQuoteCount(): Int {
        return withContext(Dispatchers.IO) {
            quoteDao.getRemoteQuoteCount()
        }
    }

    suspend fun refreshQuotes(
        limit: Int = QuotesApiService.DEFAULT_LIMIT,
        skip: Int = 0,
    ): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val response = quotesApiService.getQuotes(limit = limit, skip = skip)
                val entities = response.quotes.map { it.toEntity() }
                if (entities.isNotEmpty()) {
                    quoteDao.insertQuotes(entities)
                }
                Result.success(Unit)
            } catch (e: Exception) {
                // Network failure: return failure result while local Room DB serves cached quotes
                Result.failure(e)
            }
        }
    }

    suspend fun toggleFavorite(id: Long, isFavorite: Boolean) {
        withContext(Dispatchers.IO) {
            quoteDao.updateFavoriteStatus(id, isFavorite)
        }
    }

    suspend fun addQuote(quote: Quote): Long {
        return withContext(Dispatchers.IO) {
            quoteDao.insertQuote(quote.toEntity())
        }
    }

    suspend fun deleteQuote(quote: Quote) {
        withContext(Dispatchers.IO) {
            quoteDao.deleteQuote(quote.toEntity())
        }
    }
}
