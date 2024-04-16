package com.awesome.flickrsearch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.awesome.flickrsearch.nav.FlickrSearchNavHost
import com.awesome.flickrsearch.pages.SearchPage
import com.awesome.flickrsearch.ui.theme.FlickrSearchTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            FlickrSearchTheme {
                val controller = rememberNavController()
                FlickrSearchNavHost(navController = controller)
            }
        }
    }
}

