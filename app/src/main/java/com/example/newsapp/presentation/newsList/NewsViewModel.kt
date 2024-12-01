package com.example.newsapp.presentation.newsList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.flatMap
import androidx.paging.map
import com.example.newsapp.data.local.NewsEntity
import com.example.newsapp.data.mappers.toArticles
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject


@HiltViewModel
class NewsViewModel @Inject constructor(
    pager: Pager<Int, NewsEntity>
): ViewModel() {

    val newsPagingFlow = pager
        .flow
        .map { pagingData ->
            // Transform each NewsEntity into Articles
            pagingData.map { newsEntity ->
                newsEntity.toArticles() // Assuming toArticles returns a single Articles object
            }
        }
        .map { pagingDataOfLists ->
            // Flatten PagingData<List<Articles>> into PagingData<Articles>
            pagingDataOfLists.flatMap { articlesList -> articlesList }
        }
        .cachedIn(viewModelScope)
}