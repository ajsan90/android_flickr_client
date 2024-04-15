package com.awesome.flickrsearch.pages

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.awesome.flickrsearch.R
import com.awesome.flickrsearch.components.PhotoInfoResult
import com.awesome.flickrsearch.di.repos.ImageSearcher
import com.awesome.flickrsearch.di.vm.SearchPageState
import com.awesome.flickrsearch.ui.theme.FlickrSearchTheme
import com.awesome.flickrsearch.ui.theme.mediumWidthSmallHeight
import com.awesome.flickrsearch.ui.theme.noWidthMediumHeight
import com.awesome.flickrsearch.widgets.FlickrHeadline
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchPage(uiStateFlow: MutableStateFlow<SearchPageState>) {
    val itemsPerPage = 18
    var lastLoadedPage by remember { mutableStateOf(0) }
    var photoSearchTerms by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    val recentlySearchedTerms = remember { mutableListOf<String>() }
    var imageResultList by remember { mutableStateOf<List<PhotoInfoResult>>(listOf()) }
    val composeScope = rememberCoroutineScope()
    val gridState = rememberLazyGridState()
    val uiState by uiStateFlow.collectAsState()
    val flickrApi by remember { mutableStateOf(uiState.imageSearcher) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(Modifier.padding(8.dp)) {
            FlickrHeadline(
                name = stringResource(id = R.string.search_prompt),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Box(
                modifier = Modifier
                    .defaultMinSize(minHeight = 120.dp)
                    .fillMaxHeight(
                        if (!active) 0.15f else (0.15f * recentlySearchedTerms.size).coerceIn(
                            0.2f,
                            0.4f
                        )
                    ) //control recents height
                    .padding(noWidthMediumHeight)
            ) {
                SearchBar(
                    colors = SearchBarDefaults.colors(containerColor = Color.LightGray.copy(alpha = 0.5f)),
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxWidth(),
                    query = photoSearchTerms,
                    onQueryChange = { newQuery: String -> photoSearchTerms = newQuery },
                    onActiveChange = { activeState -> active = activeState },
                    onSearch = {
                        //enter button pressed
                        active = false
                        lastLoadedPage = 0
                        onSearchWithTerms(
                            itemsPerPage,
                            0,
                            composeScope,
                            flickrApi,
                            photoSearchTerms,
                            recentlySearchedTerms
                        ) {
                            imageResultList = it
                            //ensure we reset to top
                            composeScope.launch {
                                gridState.scrollToItem(0)
                            }
                        }
                    },
                    active = active,
                    placeholder = { Text(text = stringResource(id = R.string.search_placeholder)) },
                    leadingIcon = {
                        Icon(modifier = Modifier.clickable {
                            active = false
                            lastLoadedPage = 0
                            onSearchWithTerms(
                                itemsPerPage,
                                0,
                                composeScope,
                                flickrApi,
                                photoSearchTerms,
                                recentlySearchedTerms
                            ) {
                                imageResultList = it
                            }
                            //ensure we reset to top
                            composeScope.launch {
                                gridState.scrollToItem(0)
                            }
                        }, imageVector = Icons.Default.Search, contentDescription = "Search Button")
                    },
                    trailingIcon = {
                        if(active) {
                            Icon(
                                modifier = Modifier.clickable {
                                    if (photoSearchTerms.isNotEmpty()) {
                                        photoSearchTerms = ""
                                    } else {
                                        active = false
                                    }
                                },
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close Button"
                            )
                        }
                    },

                    ) {
                    recentlySearchedTerms.forEach {
                        Row(modifier = Modifier
                            .padding(mediumWidthSmallHeight)
                            .fillMaxWidth()
                            .clickable { photoSearchTerms = it }) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Previous Search"
                            )
                            Text(text = it, modifier = Modifier.padding(start = 16.dp))
                        }
                    }

                }
            }

            LazyVerticalGrid(
                state = gridState,
                modifier = Modifier
                    .weight(8f)
                    .background(Color.Magenta.copy(alpha = 0.1f)),
                columns = GridCells.Adaptive(minSize = 128.dp)
            ) {
                items(imageResultList.size) { index ->
                    SubcomposeAsyncImage(
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth(0.33f)
                            .height(130.dp),
                        model = imageResultList[index].mediumImageUrl,
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
                                uiState.onClickImage(imageResultList[index], gridState.firstVisibleItemIndex)
                            })
                        }
                    }
                }
            }

        }
    }
    //default search
    LaunchedEffect(Unit) {
        photoSearchTerms = flickrApi.getCachedSearchTerms()
        imageResultList = flickrApi.getCachedPhotoResultsList()
        if (imageResultList.size >= 1) {
            gridState.scrollToItem(flickrApi.getCachedScrollIndex())
        }
    }

    val visibleItems = remember { derivedStateOf { gridState.layoutInfo.visibleItemsInfo } }

    LaunchedEffect(visibleItems) {
        snapshotFlow { visibleItems.value }
            .collect { visItems ->
                if(visItems.isNotEmpty()) {
                    //load images before we get to the end of the current list
                    var shouldLoad = visibleItems.value.last().index >= imageResultList.size - 4
                    if (shouldLoad) {
                        // Load the next page data here
                        // You can call the API to fetch more data and update the imageResultList
                        var pageToLoad = (imageResultList.size/itemsPerPage)
                        if(lastLoadedPage != pageToLoad) {
                            Log.d("GridItems", "Attempting to load page $pageToLoad")
                            lastLoadedPage = pageToLoad
                            onSearchWithTerms(
                                itemsPerPage,
                                pageToLoad,
                                composeScope,
                                flickrApi,
                                photoSearchTerms,
                                null
                            ) {
                                imageResultList = imageResultList + it
                            }
                        }
                    }
                }
            }
    }
}

fun onSearchWithTerms(
    itemsPerPage: Int,
    pageIndex: Int,
    composeScope: CoroutineScope,
    searchApi: ImageSearcher,
    searchTerms: String,
    recentlySearchedTerms: MutableList<String>?,
    onNewPhotoList: (photoList: List<PhotoInfoResult>) -> Unit,
) {
    CoroutineScope(Dispatchers.IO).launch {
        if (searchTerms != "") {
            searchApi.getPhotosByTag(
                tags = searchTerms,
                page = pageIndex,
                numImagePerPage = itemsPerPage,
                onPhotoUrlResults = {
                    composeScope.launch {
                        onNewPhotoList(it)
                    }
                })
            recentlySearchedTerms?.let {
                if (recentlySearchedTerms.size >= 4) {
                    recentlySearchedTerms.removeFirst()
                }
                //Not a huge fan of this animation fix, needed time for Search Bar Transition from Active to Inactive before updating list
                CoroutineScope(Dispatchers.IO).launch {
                    delay(200)
                    recentlySearchedTerms.add(searchTerms)
                }
            }
        } else {
            Log.d("Search","attempted to search with empty terms")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchPagePreview() {
    FlickrSearchTheme {
        SearchPage(MutableStateFlow(SearchPageState(object : ImageSearcher {
            override suspend fun getPhotosByTag(
                tags: String,
                page: Int,
                numImagePerPage: Int,
                onPhotoUrlResults: (photoList: ArrayList<PhotoInfoResult>) -> Unit
            ) {
            }

            override fun cachePhotoResult(result: PhotoInfoResult, scrollIndex: Int) {
            }

            override fun getCachedPhotoResult(): PhotoInfoResult? {
                return null
            }

            override fun getCachedPhotoResultsList(): List<PhotoInfoResult> {
                return listOf()
            }

            override fun getCachedSearchTerms(): String {
                return ""
            }

            override fun getCachedScrollIndex(): Int {
                TODO("Not yet implemented")
            }

        }) {_,_ ->  }))
    }
}