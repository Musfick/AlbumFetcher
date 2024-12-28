package com.musfick.albumfetcher.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.musfick.albumfetcher.core.utils.Constants
import com.musfick.albumfetcher.core.utils.ExcludeFromJacocoGeneratedReport
import com.musfick.albumfetcher.ui.theme.AlbumFetcherTheme

@Composable
fun ErrorView(
    modifier: Modifier = Modifier,
    message:String,
    onRetry: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(message, textAlign = TextAlign.Center)
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = onRetry
        ) {
            Text("Retry")
        }
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview
@Composable
private fun ErrorViewPreview() {
    AlbumFetcherTheme {
        ErrorView(
            modifier = Modifier.background(Color.White),
            message = Constants.ERROR_OCCURRED,
            onRetry = @ExcludeFromJacocoGeneratedReport {

            }
        )
    }
}