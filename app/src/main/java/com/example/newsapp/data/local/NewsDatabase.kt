package com.example.newsapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.newsapp.data.mappers.Converters


@Database(
    entities = [NewsEntity::class],
    version = 2
)
@TypeConverters(Converters::class)
abstract class NewsDatabase : RoomDatabase() {

    abstract val dao : NewsDao
}