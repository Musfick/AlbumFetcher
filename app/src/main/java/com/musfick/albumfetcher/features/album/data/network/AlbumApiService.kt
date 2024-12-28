package com.musfick.albumfetcher.features.album.data.network

import com.musfick.albumfetcher.features.album.data.network.dto.AlbumDto
import retrofit2.Response
import retrofit2.http.GET

interface AlbumApiService {
    @GET("albums")
    suspend fun getAlbums(): Response<List<AlbumDto>>
}