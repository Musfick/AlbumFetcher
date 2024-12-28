package com.musfick.albumfetcher.features.photo.data.mapper

import com.musfick.albumfetcher.features.photo.data.network.dto.PhotoDto
import com.musfick.albumfetcher.features.photo.domain.model.Photo

fun PhotoDto.toModel() = Photo(
    albumId = albumId,
    id = id,
    title = title,
    url = url,
    thumbnailUrl = thumbnailUrl
)