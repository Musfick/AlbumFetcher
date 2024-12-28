package com.musfick.albumfetcher.features.album.domain.repository

import com.musfick.albumfetcher.features.album.domain.model.Album

interface AlbumRepository {
    suspend fun getAlbums():Result<List<Album>>
}