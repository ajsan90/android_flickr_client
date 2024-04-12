package com.awesome.flickrsearch.pages

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.awesome.flickrsearch.di.vm.DetailPageState
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun DetailPage(uiStateFlow: MutableStateFlow<DetailPageState>) {
    val uiState by uiStateFlow.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column {
            Row(modifier = Modifier.clickable {
                uiState.onBackClick()
            }) {
                Icon(
                    tint = MaterialTheme.colorScheme.primary,
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
                Text(text = "Go Back", modifier = Modifier.align(Alignment.CenterVertically))
            }
            SubcomposeAsyncImage(
                contentScale = ContentScale.FillHeight,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(1f)
                    .fillMaxHeight(0.7f),
                model = uiState.getCachedPhotoInfoResult!!.largeImageUrl,
                contentDescription = "Result"
            ) {
                val state = painter.state
                if (state is AsyncImagePainter.State.Loading || state is AsyncImagePainter.State.Error) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .width(20.dp)
                            .height(20.dp), color = MaterialTheme.colorScheme.primary
                    )
                } else {
                    SubcomposeAsyncImageContent(modifier = Modifier.clickable {
                        Log.d("Image", "Click")
                    })
                }
            }
            Card {
                Text(text = uiState.getCachedPhotoInfoResult?.title ?: "No Title")
            }
        }
    }
}