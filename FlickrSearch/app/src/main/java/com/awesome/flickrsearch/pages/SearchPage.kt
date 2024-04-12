package com.awesome.flickrsearch.pages

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.awesome.flickrsearch.R
import com.awesome.flickrsearch.ui.theme.FlickrSearchTheme
import com.awesome.flickrsearch.ui.theme.mediumWidthSmallHeight
import com.awesome.flickrsearch.ui.theme.smallWidthMediumHeight
import com.awesome.flickrsearch.widgets.FlickrHeadline
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchPage() {
    var photoSearchTerms by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    var recentlySearchedTerms = remember { mutableListOf<String>() }
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
                    .fillMaxHeight((0.15f * recentlySearchedTerms.size).coerceIn(0.2f, 0.4f)) //control recents height
                    .padding(smallWidthMediumHeight)
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
                        onSearchWithTerms(photoSearchTerms, recentlySearchedTerms)
                        photoSearchTerms = ""
                    },
                    active = active,
                    placeholder = { Text(text = stringResource(id = R.string.search_placeholder)) },
                    leadingIcon = {
                        Icon(modifier = Modifier.clickable {
                            active = false
                            onSearchWithTerms(photoSearchTerms, recentlySearchedTerms)
                            photoSearchTerms = ""
                        }, imageVector = Icons.Default.Search, contentDescription = "Search Button")
                    },
                    trailingIcon = {
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
                    },

                    ) {
                    Log.d("Recents","Recent Terms Size ${recentlySearchedTerms.size}")
                    recentlySearchedTerms.forEach {
                        Row(modifier = Modifier.padding(mediumWidthSmallHeight)) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Previous Search"
                            )
                            Text(text = it, modifier = Modifier.padding(start = 16.dp))
                        }
                    }

                }
            }
            Spacer(modifier = Modifier.weight(8f))
        }
    }
}

fun onSearchWithTerms(searchTerms: String, recentlySearchedTerms: MutableList<String>) {
    if(recentlySearchedTerms.size >= 4) {
        recentlySearchedTerms.removeFirst()
    }
    if(searchTerms != "") {
        Log.d("Add","$searchTerms")
        //Not a huge fan of this animation fix, needed time for Search Bar Transition from Active to Inactive before updating list
        CoroutineScope(Dispatchers.IO).launch {
            delay(200)
            recentlySearchedTerms.add(searchTerms)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchPagePreview() {
    FlickrSearchTheme {
        SearchPage()
    }
}