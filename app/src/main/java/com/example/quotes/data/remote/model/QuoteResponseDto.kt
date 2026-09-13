package com.example.quotes.data.remote.model

import com.google.gson.annotations.SerializedName

data class QuoteResponseDto(
    @SerializedName("quotes")
    val quotes: List<QuoteDto>,
    @SerializedName("total")
    val total: Int,
    @SerializedName("skip")
    val skip: Int,
    @SerializedName("limit")
    val limit: Int
)

data class QuoteDto(
    @SerializedName("id")
    val id: Long,
    @SerializedName("quote")
    val quote: String,
    @SerializedName("author")
    val author: String
)
