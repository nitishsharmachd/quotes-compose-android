package com.example.quotes.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.quotes.data.local.entity.QuoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuoteDao {
    @Query("SELECT * FROM quotes ORDER BY id ASC")
    fun getAllQuotes(): Flow<List<QuoteEntity>>

    @Query("SELECT * FROM quotes WHERE isFavorite = 1 ORDER BY id ASC")
    fun getFavoriteQuotes(): Flow<List<QuoteEntity>>

    @Query("SELECT * FROM quotes WHERE category = :category ORDER BY id ASC")
    fun getQuotesByCategory(category: String): Flow<List<QuoteEntity>>

    @Query("SELECT * FROM quotes WHERE text LIKE '%' || :query || '%' OR author LIKE '%' || :query || '%' ORDER BY id ASC")
    fun searchQuotes(query: String): Flow<List<QuoteEntity>>

    @Query("SELECT * FROM quotes WHERE id = :id LIMIT 1")
    fun getQuoteById(id: Long): Flow<QuoteEntity?>

    @Query("UPDATE quotes SET isFavorite = :isFavorite WHERE id = :id")
    fun updateFavoriteStatus(id: Long, isFavorite: Boolean): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertQuote(quote: QuoteEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertQuotes(quotes: List<QuoteEntity>)

    @Delete
    fun deleteQuote(quote: QuoteEntity): Int

    @Query("DELETE FROM quotes WHERE isFavorite = 0")
    fun deleteNonFavoriteQuotes(): Int

    @Query("DELETE FROM quotes")
    fun deleteAllQuotes(): Int

    @Query("SELECT COUNT(*) FROM quotes")
    fun getQuoteCount(): Int

    @Query("SELECT COUNT(*) FROM quotes WHERE isCustom = 0")
    fun getRemoteQuoteCount(): Int
}
