package com.musfick.albumfetcher.features.fetcher.domain.model

import com.musfick.albumfetcher.features.album.domain.model.Album
import com.musfick.albumfetcher.features.photo.domain.model.Photo
import com.musfick.albumfetcher.features.user.domain.model.User

class AlbumWithPhotoAndUser (
    val album: Album,
    val photo: Photo?,
    val user: User?
)