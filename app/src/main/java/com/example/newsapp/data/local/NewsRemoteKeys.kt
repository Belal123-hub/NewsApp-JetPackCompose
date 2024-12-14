package com.example.newsapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

    @Entity("remote_keys")
data class NewsRemoteKeys(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val prevPage: Int?,
    val nextPage: Int?
)
