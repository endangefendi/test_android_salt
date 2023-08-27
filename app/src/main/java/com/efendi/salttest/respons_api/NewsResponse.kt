package com.efendi.salttest.respons_api

import com.efendi.salttest.models.Article

data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>
)
