package com.example.quotes.di

import android.content.Context
import androidx.room.Room
import com.example.quotes.data.local.dao.QuoteDao
import com.example.quotes.data.local.database.QuotesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideQuotesDatabase(
        @ApplicationContext context: Context
    ): QuotesDatabase {
        return Room.databaseBuilder(
            context,
            QuotesDatabase::class.java,
            QuotesDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration()
         .build()
    }

    @Provides
    @Singleton
    fun provideQuoteDao(database: QuotesDatabase): QuoteDao {
        return database.quoteDao()
    }
}
