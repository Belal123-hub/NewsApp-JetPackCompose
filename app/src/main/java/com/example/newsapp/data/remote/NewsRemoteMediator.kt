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
    private val newsDb: NewsDatabase,
    private val newsApi: NewsApi
) : RemoteMediator<Int, NewsEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, NewsEntity>
    ): MediatorResult {
        return try {
            // Get the appropriate page number
            val loadKey = when (loadType) {
                LoadType.REFRESH -> 1 // Start from page 1
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    if (lastItem == null) {
                        1 // First page if no items loaded
                    } else {
                        val nextPage = lastItem.totalResults?.let { totalResults ->
                            // Calculate next page number based on total results and page size
                            (totalResults / state.config.pageSize) + 1
                        } ?: 1
                        nextPage
                    }
                }
            }
            Log.d("Pagination", "LoadKey: $loadKey")

            // Fetch news from the API with the correct page number and page size
            val newsDto = newsApi.getNews(
                page = loadKey,
                pageCount = state.config.pageSize
            )

            Log.d(
                "NewsApiResponse",
                "Page: $loadKey, Total articles: ${newsDto.totalResults}, Articles: ${newsDto.articles.size}"
            )

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
                status = newsDto.status,
                totalResults = newsDto.totalResults,
                articles = newsEntities // List of articles directly
            )

            // Upsert the data into the database
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

    fun getKey(state: PagingState<Int, NewsEntity>): Int? {
        // Use the key from the last item or return null if it can't be determined
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.totalResults
        }
    }
}

