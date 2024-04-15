package com.awesome.flickrsearch.di.vm

import androidx.lifecycle.ViewModel
import com.awesome.flickrsearch.components.PhotoInfoResult
import com.awesome.flickrsearch.di.repos.ImageSearcher
import com.awesome.flickrsearch.nav.FsDestinations
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class SearchPageVM @Inject constructor(val imageSearcher: ImageSearcher
) : ViewModel() {
  var navigateTo: (destination: FsDestinations) -> Unit = {}
    set(value) {
      field = value
      uiStateFlow.value = SearchPageState(imageSearcher) { photo, scrollIndex ->
        imageSearcher.cachePhotoResult(photo, scrollIndex)
        navigateTo(FsDestinations.DetailPage)
      }
    }

  val uiStateFlow = MutableStateFlow<SearchPageState>(SearchPageState(imageSearcher) { _,_ -> })
}

data class SearchPageState(val imageSearcher: ImageSearcher, val onClickImage: (PhotoInfoResult, Int) -> Unit,)