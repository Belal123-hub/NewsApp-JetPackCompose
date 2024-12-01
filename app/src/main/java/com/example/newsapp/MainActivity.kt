package com.example.newsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.newsapp.presentation.SplashScreen
import com.example.newsapp.presentation.newsList.NewsScreen
import com.example.newsapp.presentation.newsList.NewsViewModel
import com.example.newsapp.ui.theme.NewsAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewsAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // State variable to manage splash screen visibility
                    var isSplashVisible by remember { mutableStateOf(true) }

                    // Launch a coroutine to simulate loading time
                    LaunchedEffect(Unit) {
                        delay(2000) // Show splash for 2 seconds
                        isSplashVisible = false
                    }

                    // Show SplashScreen or NewsScreen based on the state
                    if (isSplashVisible) {
                        SplashScreen()
                    } else {
                        val viewModel = hiltViewModel<NewsViewModel>()
                        val news = viewModel.newsPagingFlow.collectAsLazyPagingItems()
                        NewsScreen(news = news)
                    }
                }
            }
        }
    }
}