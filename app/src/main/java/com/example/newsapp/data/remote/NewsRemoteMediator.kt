package com.example.newsapp.data.remote

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState.Loading.endOfPaginationReached
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.newsapp.data.local.ArticlesEntity
import com.example.newsapp.data.local.NewsDatabase
import com.example.newsapp.data.local.NewsEntity
import com.example.newsapp.data.local.NewsRemoteKeys
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class NewsRemoteMediator(
    private val newsDb: NewsDatabase,
    private val newsApi: NewsApi
) : RemoteMediator<Int, NewsEntity>() {

    private val newsRemoteKeysDao = newsDb.newsRemoteKeysDao()
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
                    val nextPageFromDb = newsRemoteKeysDao.getLatestNextPage()
                    val nextPage = if (nextPageFromDb != null && nextPageFromDb <= 5) {
                        nextPageFromDb
                    } else {
                        5
                    }
                    nextPage
                }

            }
            Log.d("Pagination", "LoadKey: $loadKey")

            // Fetch news from the API with the correct page number and page size
            val newsDto = newsApi.getNews(
                page = loadKey,

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
                    newsRemoteKeysDao.deleteAllRemoteKeys()
                }
                val prevPage = if (loadKey == 1 || loadKey == 0) null else loadKey - 1
                val nextPage = if (endOfPaginationReached) null else loadKey + 1
                val newsRemoteKeys = NewsRemoteKeys(
                    id = 1,
                    prevPage= prevPage,
                    nextPage = nextPage
                )
                // Upsert the NewsEntity which contains the list of ArticlesEntity
                newsDb.dao.upsertAll(listOf(newsEntity)) // Wrap newsEntity in a list
                newsRemoteKeysDao.addALlRemoteKeys(newsRemoteKeys)
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
