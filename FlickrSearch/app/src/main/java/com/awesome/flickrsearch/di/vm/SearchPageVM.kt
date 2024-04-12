package com.awesome.flickrsearch.di.vm

import androidx.lifecycle.ViewModel
import com.awesome.flickrsearch.components.PhotoUrlResult
import com.awesome.flickrsearch.di.SimpleModule
import com.awesome.flickrsearch.nav.FsDestinations
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class SearchPageVM @Inject constructor(val imageSearcher: SimpleModule.ImageSearcher
) : ViewModel() {
  var navigateTo: (destination: FsDestinations) -> Unit = {}
    set(value) {
      field = value
      uiStateFlow.value = SearchPageState { navigateTo(FsDestinations.DetailPage) }
    }

  val uiStateFlow = MutableStateFlow<SearchPageState>(SearchPageState {  })
}

data class SearchPageState(val onClickImage: (PhotoUrlResult) -> Unit)