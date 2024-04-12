package com.awesome.flickrsearch.di.vm

import androidx.lifecycle.ViewModel
import com.awesome.flickrsearch.components.PhotoUrlResult
import com.awesome.flickrsearch.di.SimpleModule
import com.awesome.flickrsearch.nav.FsDestinations
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class DetailPageVM @Inject constructor(val imageSearcher: SimpleModule.ImageSearcher
) : ViewModel() {
    var goBack: () -> Unit = {}
        set(value) {
            field = value
            uiStateFlow.value = DetailPageState { goBack() }
        }

    val uiStateFlow = MutableStateFlow<DetailPageState>(DetailPageState {  })
}

data class DetailPageState(val onBackClick: ()-> Unit)