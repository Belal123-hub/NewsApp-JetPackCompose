package com.example.newsapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.newsapp.data.mappers.Converters


data class ArticlesEntity (
    val author      : String? = null,
    val title       : String? = null,
    val description : String? = null,
    val url         : String? = null,
    val urlToImage  : String? = null,
    val publishedAt : String? = null,
    val content     : String? = null
)

@Entity
@TypeConverters(Converters::class)
data class NewsEntity(
    @PrimaryKey(autoGenerate = false)
    val status: String,
    val totalResults: Int?= null,
    val articles: List<ArticlesEntity> = listOf()
)
