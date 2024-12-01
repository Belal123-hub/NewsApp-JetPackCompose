package com.example.newsapp.data.mappers

import com.example.newsapp.data.local.ArticlesEntity
import com.example.newsapp.data.local.NewsEntity
import com.example.newsapp.data.remote.NewsDto
import com.example.newsapp.domain.Articles

fun NewsDto.toNewsEntity() : NewsEntity{
    return NewsEntity(
        status = status,
        totalResults = totalResults,
        articles = ArrayList(articles.map { article ->
            ArticlesEntity(
                author = article.author,
                title = article.title,
                description = article.description,
                url = article.url,
                urlToImage = article.urlToImage,
                publishedAt = article.publishedAt,
                content = article.content
            )
        })
    )
}

fun NewsEntity.toArticles() : List<Articles> {
    return articles.map { article ->
            Articles(
                author = article.author,
                title = article.title,
                description = article.description,
                url = article.url,
                urlToImage = article.urlToImage,
                publishedAt = article.publishedAt,
                content = article.content
            )
        }
}