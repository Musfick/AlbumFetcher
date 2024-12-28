package com.musfick.albumfetcher.features.photo.domain.repository

import com.musfick.albumfetcher.features.photo.domain.model.Photo

interface PhotoRepository {
    suspend fun getPhotos():Result<List<Photo>>
}