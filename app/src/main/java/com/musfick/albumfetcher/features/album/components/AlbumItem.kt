package com.musfick.albumfetcher.features.album.components

import android.provider.CalendarContract.Colors
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.musfick.albumfetcher.core.utils.ExcludeFromJacocoGeneratedReport
import com.musfick.albumfetcher.core.utils.Faker
import com.musfick.albumfetcher.features.album.domain.model.Album
import com.musfick.albumfetcher.features.fetcher.domain.model.AlbumWithPhotoAndUser
import com.musfick.albumfetcher.features.user.domain.model.User
import com.musfick.albumfetcher.ui.theme.AlbumFetcherTheme

@Composable
fun AlbumItem(
    modifier: Modifier = Modifier,
    item: AlbumWithPhotoAndUser
) {
    var imageLoaded by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier
            .padding(8.dp)
            .height(130.dp)
            .fillMaxWidth()
    ){
        Row {

            Box {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(100.dp),
                    model = item.photo?.thumbnailUrl,
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                    onSuccess = {
                        imageLoaded = true
                    }
                )
                if(!imageLoaded){
                    Box(modifier = Modifier
                        .fillMaxHeight()
                        .width(100.dp).background(Color.LightGray)
                        , contentAlignment = Alignment.Center){
                        CircularProgressIndicator()
                    }
                }
            }

            Spacer(Modifier.width(8.dp))
            Column(
                modifier = Modifier.weight(1f).fillMaxWidth().align(Alignment.Bottom)
            ) {
                Text(item.photo?.title ?: "N/A", style = MaterialTheme.typography.titleMedium)
                Text(item.album.title, style = MaterialTheme.typography.bodyMedium)
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        imageVector = Icons.Default.Person, contentDescription = null
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(item.user?.username ?: "N/A", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview
@Composable
private fun AlbumPreview() {
    AlbumFetcherTheme {
        AlbumItem(
            modifier = Modifier.background(Color.White),
            item = AlbumWithPhotoAndUser(
                album = Faker.getAlbum(),
                user = Faker.getUser(),
                photo = Faker.getPhoto()
            )
        )
    }
}