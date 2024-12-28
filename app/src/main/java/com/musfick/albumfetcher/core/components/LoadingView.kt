package com.musfick.albumfetcher.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.musfick.albumfetcher.core.utils.ExcludeFromJacocoGeneratedReport
import com.musfick.albumfetcher.ui.theme.AlbumFetcherTheme

@Composable
fun LoadingView(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        Spacer(Modifier.height(8.dp))
        Text("Loading...")
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview
@Composable
private fun LoadingViewPreview() {
    AlbumFetcherTheme {
        LoadingView(
            modifier = Modifier.background(Color.White),
        )
    }
}