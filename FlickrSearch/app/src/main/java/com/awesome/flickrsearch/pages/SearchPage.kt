package com.awesome.flickrsearch.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.awesome.flickrsearch.R
import com.awesome.flickrsearch.ui.theme.FlickrSearchTheme
import com.awesome.flickrsearch.ui.theme.smallWidthTallHeight
import com.awesome.flickrsearch.widgets.FlickrHeadline

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchPage() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column {
            FlickrHeadline(
                name = stringResource(id = R.string.search_prompt),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            SearchBar(
                modifier = Modifier
                    .weight(1f)
                    .padding(smallWidthTallHeight),
                query = "",
                onQueryChange = {newQuery: String -> },
                onSearch = {},
                active = true,
                onActiveChange = {isActive: Boolean -> }
            ) {

            }
            Spacer(modifier = Modifier.weight(8f))
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