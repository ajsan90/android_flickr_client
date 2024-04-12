package com.awesome.flickrsearch.di.vm

import androidx.lifecycle.ViewModel
import com.awesome.flickrsearch.components.PhotoInfoResult
import com.awesome.flickrsearch.di.repos.ImageSearcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class DetailPageVM @Inject constructor(val imageSearcher: ImageSearcher
) : ViewModel() {
    var goBack: () -> Unit = {}
        set(value) {
            field = value
            uiStateFlow.value = DetailPageState(imageSearcher.getCachedPhotoResult()) { goBack() }
        }

    val uiStateFlow = MutableStateFlow<DetailPageState>(DetailPageState(imageSearcher.getCachedPhotoResult()) {  })
}

data class DetailPageState(val getCachedPhotoInfoResult: PhotoInfoResult?, val onBackClick: ()-> Unit)