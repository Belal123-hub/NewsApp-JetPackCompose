package com.example.newsapp.presentation.newsList


import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.newsapp.domain.Articles


@Composable
fun NewsItem(
    news: Articles,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
                .padding(16.dp)
        ) {
            AsyncImage(
                model = news.urlToImage,
                contentDescription = null,
                modifier = Modifier
                    .weight(1f)
                    .height(150.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier
                    .weight(3f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                val sourceName = news

                if (sourceName != null) {
                    sourceName.author?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                        Spacer(modifier = Modifier.height(8.dp))
                    sourceName.title?.let {
                        Text(
                            text = it,
                            fontStyle = FontStyle.Italic,
                            color = Color.LightGray,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                        Spacer(modifier = Modifier.height(8.dp))
                    sourceName.description?.let {
                        Text(
                            text = it,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "published at  ${sourceName.publishedAt}",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.End,
                            fontSize = 8.sp
                        )

                }
            }
        }
    }
}