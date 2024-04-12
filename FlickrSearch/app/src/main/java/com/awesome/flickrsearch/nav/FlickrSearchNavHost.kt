package com.awesome.flickrsearch.nav

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.awesome.flickrsearch.di.vm.DetailPageVM
import com.awesome.flickrsearch.di.vm.SearchPageVM
import com.awesome.flickrsearch.pages.DetailPage
import com.awesome.flickrsearch.pages.SearchPage


@Composable
fun FlickrSearchNavHost(navController: NavHostController){
    NavHost(startDestination = FsDestinations.SearchPage.key, navController = navController) {
        composable(FsDestinations.SearchPage.key) {
            val searchPageVM = hiltViewModel<SearchPageVM>()
            searchPageVM.navigateTo = { dest: FsDestinations -> navController.navigate(dest.key)}
            SearchPage(searchPageVM.uiStateFlow)
        }
        composable(FsDestinations.DetailPage.key) {
            val detailPageVM = hiltViewModel<DetailPageVM>()
            detailPageVM.goBack = {navController.popBackStack()}
            DetailPage(detailPageVM.uiStateFlow)
        }
    }
}

enum class FsDestinations(val key: String) {
    SearchPage("searchPage"),
    DetailPage("detailPage")
}
