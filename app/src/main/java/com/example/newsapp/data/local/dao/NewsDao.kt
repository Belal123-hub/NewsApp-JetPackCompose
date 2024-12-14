package com.example.newsapp.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.newsapp.data.local.NewsEntity


@Dao
interface NewsDao {
    @Upsert
    suspend fun upsertAll(news: kotlin.collections.List<com.example.newsapp.data.local.NewsEntity>)

    @Query("SELECT * FROM newsentity ")
    fun pagingSource(): PagingSource<Int , NewsEntity>

    @Query("DELETE FROM newsentity")
    suspend fun delete()
}