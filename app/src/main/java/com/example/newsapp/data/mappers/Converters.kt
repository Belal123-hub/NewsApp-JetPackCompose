package com.example.newsapp.data.mappers

import androidx.room.TypeConverter
import com.example.newsapp.data.local.ArticlesEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    private val gson = Gson()

    // Convert List<ArticlesEntity> to String (JSON)
    @TypeConverter
    fun fromArticlesList(articles: List<ArticlesEntity>?): String? {
        return gson.toJson(articles)
    }

    // Convert String (JSON) back to List<ArticlesEntity>
    @TypeConverter
    fun toArticlesList(data: String?): List<ArticlesEntity>? {
        val listType = object : TypeToken<List<ArticlesEntity>>() {}.type
        return gson.fromJson(data, listType)
    }
}
