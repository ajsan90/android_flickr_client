package com.awesome.flickrsearch.widgets

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import com.awesome.flickrsearch.ui.theme.Typography

@Composable
fun FlickrHeadline(name: String, modifier: Modifier = Modifier) {
    Text(
        fontFamily = FontFamily.Cursive,
        style = Typography.headlineLarge,
        text = name,
        modifier = modifier
    )
}