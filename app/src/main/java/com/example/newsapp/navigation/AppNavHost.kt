import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.newsapp.navigation.AppScreens
import com.example.newsapp.presentation.newsList.NewsScreen
import com.example.newsapp.presentation.newsList.NewsViewModel
import com.example.newsapp.presentation.webview.WebViewScreen


@Composable
fun AppNavigation(viewModel: NewsViewModel) {
    val navController = rememberNavController()
    val news = viewModel.newsPagingFlow.collectAsLazyPagingItems()

    NavHost(navController = navController, startDestination = AppScreens.NewsScreen.route) {
        composable(route = AppScreens.NewsScreen.route) {
            NewsScreen(
                navController = navController,
                news = news,

            )
        }
        composable(
            route = AppScreens.WebViewScreen.route,
            arguments = listOf(navArgument("url") { type = NavType.StringType })
        ) { backStackEntry ->
            val url = backStackEntry.arguments?.getString("url")
            WebViewScreen(navController = navController, url = url)
        }
    }
}
