package com.awesome.flickrsearch.di.vm

import PhotoUrlResult
import androidx.lifecycle.ViewModel
import com.awesome.flickrsearch.nav.FsDestinations
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class SearchPageVM @Inject constructor(
) : ViewModel() {
  var navigateTo: (destination: FsDestinations) -> Unit = {}
  val uiStateFlow = MutableStateFlow<SearchPageState>(SearchPageState {  })
  init {

  }

}

data class SearchPageState(val onClickImage: (PhotoUrlResult) -> Unit)