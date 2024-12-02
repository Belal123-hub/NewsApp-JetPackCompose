package com.example.newsapp.navigation

sealed class AppScreens (val route: String){
    object NewsScreen: AppScreens ("news_screen")
    object WebViewScreen: AppScreens ("webview_screen")
}