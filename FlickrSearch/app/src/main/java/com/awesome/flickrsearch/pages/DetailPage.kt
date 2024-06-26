package com.awesome.flickrsearch.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.awesome.flickrsearch.R
import com.awesome.flickrsearch.di.vm.DetailPageState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import com.awesome.flickrsearch.ui.theme.Typography
import com.awesome.flickrsearch.ui.theme.mediumWidthMicroHeight
import com.awesome.flickrsearch.ui.theme.smallWidthMediumHeight
import java.text.SimpleDateFormat

@Composable
fun DetailPage(uiStateFlow: MutableStateFlow<DetailPageState>) {
    val uiState by uiStateFlow.collectAsState()
    var angle by remember { mutableStateOf(0f) }
    var zoom by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    val sdf by remember { mutableStateOf(SimpleDateFormat("dd/mm/yyyy"))}
    var datePosted by remember { mutableStateOf("") }
    var dateTaken by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column {
            //
            Column {
                Row(modifier = Modifier
                    .padding(4.dp)
                    .clickable {
                        uiState.onBackClick()
                    }) {
                    Icon(
                        tint = MaterialTheme.colorScheme.primary,
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                    Text(text = stringResource(id = R.string.go_back), modifier = Modifier.align(Alignment.CenterVertically))
                }
                Box(
                    modifier = Modifier
                        .background(Color.Black)
                        .clip(RectangleShape) // Clip the box content // Give the size you want...

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
                                .offset {
                                    IntOffset(
                                        offsetX.roundToInt(),
                                        offsetY.roundToInt()
                                    )
                                }
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
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .background(Color.Black.copy(alpha = 0.5f))
                    ) {
                        Text(
                            text = stringResource(id = R.string.pinch_to_zoom), color = Color.White, modifier = Modifier.align(
                                Alignment.CenterVertically
                            )
                        )
                    }
                }
            }
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Card(
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
                    modifier = Modifier
                        .padding(mediumWidthMicroHeight)
                        .fillMaxWidth(1f)
                ) {
                    Column {
                        Text(
                            fontWeight = FontWeight.Bold,
                            style = Typography.titleSmall,
                            modifier = Modifier
                                .padding(top = 16.dp, start = 16.dp),
                            text = stringResource(id = R.string.title)
                        )
                        Text(
                            style = Typography.titleMedium,
                            modifier = Modifier
                                .align(Alignment.Start)
                                .padding(start = 16.dp),
                            text = uiState.getCachedPhotoInfoResult?.title ?: stringResource(id = R.string.no_title)
                        )
                        Text(
                            style = Typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .align(Alignment.Start)
                                .padding(top = 8.dp, start = 16.dp), text = stringResource(id = R.string.description)
                        )
                        var description = ""
                        uiState?.getCachedPhotoInfoResult?.description?.let {desc ->
                            if (desc.isNotBlank()) {
                                description = desc
                            }
                        }
                        Text(
                            style = Typography.titleSmall,
                            modifier = Modifier
                                .align(Alignment.Start)
                                .padding(start = 16.dp),
                            text = description
                        )
                        Row(modifier = Modifier.align(Alignment.Start)) {
                            Text(
                                style = Typography.titleSmall, modifier = Modifier
                                    .padding(start = 16.dp), text = stringResource(id = R.string.photo_taken)
                            )
                            Text(
                                style = Typography.titleSmall,
                                modifier = Modifier
                                    .padding(start = 16.dp),
                                text = dateTaken
                            )
                        }
                        Row(modifier = Modifier.align(Alignment.Start)) {
                            Text(
                                style = Typography.titleSmall, modifier = Modifier
                                    .padding(start = 16.dp), text = stringResource(id = R.string.posted)
                            )
                            Text(
                                style = Typography.titleSmall,
                                modifier = Modifier
                                    .padding(start = 16.dp, bottom = 8.dp),
                                text = datePosted
                            )
                        }
                    }
                }
            }

        }
    }
    LaunchedEffect(Unit)
    {
        uiState.getCachedPhotoInfoResult?.datePosted?.let {
            datePosted = sdf.format(it)
        }
        uiState.getCachedPhotoInfoResult?.dateTaken?.let {
            dateTaken = sdf.format(it)
        }
    }
}