package com.awesome.flickrsearch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.awesome.flickrsearch.pages.SearchPage
import com.awesome.flickrsearch.ui.theme.FlickrSearchTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlickrSearchTheme {
                SearchPage()
            }
        }
    }
}

