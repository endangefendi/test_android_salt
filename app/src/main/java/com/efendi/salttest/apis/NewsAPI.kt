package com.efendi.salttest.apis

import com.efendi.salttest.respons_api.NewsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {
    @GET("v2/top-headlines")
    fun getTopHeadlines(
        @Query("country") country: String,
        @Query("apiKey") apiKey: String,
        @Query("page") page: Int
    ): Call<NewsResponse>
}
