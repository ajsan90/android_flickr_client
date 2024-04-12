package com.awesome.flickrsearch.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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
            Text(text = "Detail Page")
            Button(onClick = {
                uiState.onBackClick()
            }) {
                Text(text = "Go Back")
            }
        }
    }
}