package com.musfick.albumfetcher.features.album.data.mapper

import com.musfick.albumfetcher.features.album.data.network.dto.AlbumDto
import com.musfick.albumfetcher.features.album.domain.model.Album

fun AlbumDto.toModel() = Album(
    id = id,
    title = title,
    userId = userId,
)