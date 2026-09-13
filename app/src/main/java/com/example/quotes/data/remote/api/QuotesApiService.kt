package com.example.quotes.data.remote.api

import com.example.quotes.data.remote.model.QuoteResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface QuotesApiService {
    @GET("quotes")
    suspend fun getQuotes(
        @Query("limit") limit: Int = DEFAULT_LIMIT,
        @Query("skip") skip: Int = 0,
    ): QuoteResponseDto

    companion object {
        const val BASE_URL = "https://dummyjson.com/"
        const val DEFAULT_LIMIT = 30
    }
}
