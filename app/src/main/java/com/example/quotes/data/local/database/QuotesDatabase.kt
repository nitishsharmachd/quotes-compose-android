package com.example.quotes.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.quotes.data.local.dao.QuoteDao
import com.example.quotes.data.local.entity.QuoteEntity

@Database(entities = [QuoteEntity::class], version = 2, exportSchema = false)
abstract class QuotesDatabase : RoomDatabase() {
    abstract fun quoteDao(): QuoteDao

    companion object {
        const val DATABASE_NAME = "quotes_db"
    }
}
