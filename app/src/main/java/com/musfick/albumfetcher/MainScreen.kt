package com.musfick.albumfetcher
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.musfick.albumfetcher.core.components.ErrorView
import com.musfick.albumfetcher.core.components.LoadingView
import com.musfick.albumfetcher.features.album.components.AlbumItem

@Composable
fun MainScreen(
    uiState: MainUiState,
    onRetry: () -> Unit
) {
    Scaffold {
        Box(modifier = Modifier.padding(it)){
            when(uiState){
                is MainUiState.Loading -> LoadingView(
                    modifier = Modifier.testTag("loading_view")
                )
                is MainUiState.Success -> {
                    LazyColumn {
                        items(uiState.items){
                            AlbumItem(item = it)
                        }
                    }
                }
                is MainUiState.Error -> {
                    ErrorView(
                        message = uiState.message,
                        onRetry = onRetry
                    )
                }
            }
        }
    }
}
