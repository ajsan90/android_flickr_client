package com.awesome.flickrsearch.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.awesome.flickrsearch.di.vm.DetailPageState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import com.awesome.flickrsearch.ui.theme.Typography

@Composable
fun DetailPage(uiStateFlow: MutableStateFlow<DetailPageState>) {
    val uiState by uiStateFlow.collectAsState()
    var angle by remember { mutableStateOf(0f) }
    var zoom by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
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
            Box(
                modifier = Modifier
                    .clip(RectangleShape) // Clip the box content // Give the size you want...
                    .background(MaterialTheme.colorScheme.background)

            ) {
                SubcomposeAsyncImage(
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.Center) // keep the image centralized into the Box
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
                        //ideas pulled from https://stackoverflow.com/questions/66005066/android-jetpack-compose-how-to-zoom-a-image-in-a-box
                        SubcomposeAsyncImageContent(modifier = Modifier
                            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                            .graphicsLayer(
                                scaleX = zoom,
                                scaleY = zoom,
                                rotationZ = angle
                            )
                            .pointerInput(Unit) {
                                detectTransformGestures(
                                    onGesture = { _, pan, gestureZoom, gestureRotate ->
                                        angle += gestureRotate
                                        zoom *= gestureZoom
                                        val x = pan.x * zoom
                                        val y = pan.y * zoom
                                        val angleRad = angle * PI / 180.0
                                        offsetX += (x * cos(angleRad) - y * sin(angleRad)).toFloat()
                                        offsetY += (x * sin(angleRad) + y * cos(angleRad)).toFloat()
                                    }
                                )
                            })
                    }
                }
            }
            Card(modifier = Modifier.padding(16.dp)) {
                Column {
                    Text(style = Typography.titleLarge, modifier = Modifier.align(Alignment.CenterHorizontally).padding(16.dp), text = uiState.getCachedPhotoInfoResult?.title ?: "No Title")
                }
            }
        }
    }
}