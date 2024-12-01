//package com.example.newsapp.navigation
//
//import androidx.compose.runtime.Composable
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.paging.compose.LazyPagingItems
//import com.example.newsapp.domain.Articles
//import com.example.newsapp.presentation.newsList.NewsScreen
//import com.example.newsapp.presentation.webview.WebViewScreen
//
//@Composable
//fun NavGraph(navController: NavHostController, news: LazyPagingItems<Articles>) {
//    NavHost(navController = navController, startDestination = "news") {
//        composable("news") {
//            NewsScreen(navController = navController, news = news)
//        }
//        composable("webview/{url}") { backStackEntry ->
//            val url = backStackEntry.arguments?.getString("url")
//            if (url != null) {
//                WebViewScreen(url = url)
//            }
//        }
//    }
//}