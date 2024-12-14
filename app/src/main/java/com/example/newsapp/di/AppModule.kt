package com.example.newsapp.di

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.Room
import com.example.newsapp.data.local.NewsDatabase
import com.example.newsapp.data.local.NewsEntity
import com.example.newsapp.data.remote.NewsApi
import com.example.newsapp.data.remote.NewsRemoteMediator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton


@OptIn(ExperimentalPagingApi::class)
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideNewsDatabase(@ApplicationContext context: Context): NewsDatabase {
        return Room.databaseBuilder(
            context,
            NewsDatabase::class.java,
            "news.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
    @Provides
    @Singleton
    fun provideNewsApi(): NewsApi {
        return Retrofit.Builder()
            .baseUrl(NewsApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create()
    }
    @Provides
    @Singleton
    fun provideNewsPager(newsDb: NewsDatabase, newsApi: NewsApi): Pager<Int, NewsEntity> {
        return Pager(
            config = PagingConfig(
                pageSize = 500,
                enablePlaceholders = false,
                prefetchDistance = 5
            ),
            remoteMediator = NewsRemoteMediator(
                newsDb = newsDb,
                newsApi = newsApi
            ),
            pagingSourceFactory = {
                newsDb.dao.pagingSource()
            }
        )
    }
}
