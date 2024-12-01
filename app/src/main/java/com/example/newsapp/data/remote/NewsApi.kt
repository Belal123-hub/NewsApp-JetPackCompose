package com.example.newsapp.data.remote


import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("everything")
    suspend fun getNews(
        @Query("q") query: String = "ios", // Query parameter for the search term
        @Query("from") from: String = "2019-04-00", // Query parameter for the date
        @Query("sortBy") sortBy: String = "publishedAt", // Query parameter for sorting
        @Query("apiKey") apiKey: String = "26eddb253e7840f988aec61f2ece2907", // Your API key
        @Query("page") page: Int, // Page number for pagination
        @Query("per_page") pageCount: Int // Number of articles per page
    ): NewsDto

    companion object {
        const val BASE_URL = "https://newsapi.org/v2/"
    }
}