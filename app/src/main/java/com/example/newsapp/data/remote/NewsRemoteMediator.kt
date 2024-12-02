package com.example.newsapp.data.remote

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.newsapp.data.local.ArticlesEntity
import com.example.newsapp.data.local.NewsDatabase
import com.example.newsapp.data.local.NewsEntity
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class NewsRemoteMediator(
    private val newsDb : NewsDatabase,
    private val newsApi : NewsApi
) : RemoteMediator< Int , NewsEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, NewsEntity>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(
                    endOfPaginationReached = true
                )
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    if (lastItem == null) {
                        1
                    } else {
                        lastItem.totalResults?.let { totalResults ->
                            if (totalResults > state.config.pageSize) {
                                (totalResults / state.config.pageSize) + 1
                            } else {
                                return MediatorResult.Success(endOfPaginationReached = true)
                            }
                        } ?: 1
                    }
                }
            }

            // Fetch news from API
            val newsDto = newsApi.getNews(
                page = loadKey,
                pageCount = state.config.pageSize
            )
            Log.d("NewsApiResponse", "Page: $loadKey, Total articles: ${newsDto.totalResults}, Articles: ${newsDto.articles.size}")


            // Create a list of ArticlesEntity from the articles in newsDto
            val newsEntities = newsDto.articles.map { article ->
                ArticlesEntity(
                    author = article.author,
                    title = article.title,
                    description = article.description,
                    url = article.url,
                    urlToImage = article.urlToImage,
                    publishedAt = article.publishedAt,
                    content = article.content
                )
            }

            // Create a NewsEntity that includes the list of ArticlesEntity
            val newsEntity = NewsEntity(
               // Handle this for unique IDs
                status = newsDto.status,
                totalResults = newsDto.totalResults,
                articles = ArrayList(newsEntities) // Convert List to ArrayList
            )

            newsDb.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    newsDb.dao.delete() // Clear existing data if refreshing
                }
                // Upsert the NewsEntity which contains the list of ArticlesEntity
                newsDb.dao.upsertAll(listOf(newsEntity)) // Wrap newsEntity in a list
            }

            MediatorResult.Success(
                endOfPaginationReached = newsDto.articles.isEmpty() // Check if articles are empty
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}