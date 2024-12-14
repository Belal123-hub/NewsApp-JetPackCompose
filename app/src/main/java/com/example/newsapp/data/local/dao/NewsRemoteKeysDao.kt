package com.example.newsapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.newsapp.data.local.NewsRemoteKeys

@Dao
interface NewsRemoteKeysDao {

    @Query("SELECT * FROM remote_keys WHERE id = :id")
    suspend fun getRemoteKeys(id: String): NewsRemoteKeys?

    @Query("SELECT nextPage FROM remote_keys ORDER BY id DESC LIMIT 1")
    suspend fun getLatestNextPage(): Int?
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addALlRemoteKeys(remoteKeys: com.example.newsapp.data.local.NewsRemoteKeys)

    @Query("DELETE FROM remote_keys")
    suspend fun deleteAllRemoteKeys()
}
