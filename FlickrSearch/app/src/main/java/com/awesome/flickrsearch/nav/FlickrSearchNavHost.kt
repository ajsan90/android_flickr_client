package com.awesome.flickrsearch.nav

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.awesome.flickrsearch.di.vm.SearchPageVM
import com.awesome.flickrsearch.pages.SearchPage


@Composable
fun FlickrSearchNavHost(navController: NavHostController){
    NavHost(startDestination = "searchPage", navController = navController) {
        composable("searchPage") {
            val searchPageVM = hiltViewModel<SearchPageVM>()
            searchPageVM.navigateTo = { dest: FsDestinations -> navController.navigate(dest.key)}
            SearchPage()
        }
        composable("detailPage") {
            val searchPageVM = hiltViewModel<SearchPageVM>()
            SearchPage()
        }
    }
}

enum class FsDestinations(val key: String) {
    SearchPage("searchPage"),
    DestinationPage("destination")
}
