package com.example.newsapp.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.newsapp.R

@Composable
fun SplashScreen() {
    // Define the gradient colors
    val listColors = listOf(
        Color(android.graphics.Color.rgb(234, 127, 127)),
        Color(android.graphics.Color.rgb(132, 71, 71))
    )

    // Create a Column with a gradient background
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.linearGradient(listColors)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top // Change to Top to keep images at the top
    ) {
        Image(
            painter = painterResource(R.drawable.educatedcat),
            contentDescription = null,
            modifier = Modifier
                .padding(top = 50.dp)
                .width(277.dp)
                .height(250.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Image(
            painter = painterResource(R.drawable.catnews),
            contentDescription = null,
            modifier = Modifier
                .width(300.dp)
                .height(100.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}