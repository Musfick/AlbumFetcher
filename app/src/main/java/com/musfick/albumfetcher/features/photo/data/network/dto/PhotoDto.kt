package com.musfick.albumfetcher.features.photo.data.network.dto

data class PhotoDto(
    val albumId: Int,
    val id: Int,
    val thumbnailUrl: String,
    val title: String,
    val url: String
)