package com.musfick.albumfetcher.features.fetcher.utils

import com.musfick.albumfetcher.features.fetcher.domain.model.AlbumWithPhotoAndUser

sealed class FetchState {
    data object Loading: FetchState()
    data class Success(val items:List<AlbumWithPhotoAndUser>): FetchState()
    data class Error(val message:String): FetchState()
}