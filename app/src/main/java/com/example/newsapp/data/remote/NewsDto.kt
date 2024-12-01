package com.example.newsapp.data.remote



data class ArticlesDto (
    val author      : String? = null,
    val title       : String? = null,
    val description : String? = null,
    val url         : String? = null,
    val urlToImage  : String? = null,
    val publishedAt : String? = null,
    val content     : String? = null
)

data class NewsDto(
    val status: String,
    val totalResults: Int?= null,
    val articles: List<ArticlesDto> = listOf()
)
