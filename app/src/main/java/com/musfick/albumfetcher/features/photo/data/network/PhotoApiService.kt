package com.musfick.albumfetcher.features.photo.data.network
import com.musfick.albumfetcher.features.photo.data.network.dto.PhotoDto
import retrofit2.Response
import retrofit2.http.GET

interface PhotoApiService {
    @GET("photos")
    suspend fun getPhotos(): Response<List<PhotoDto>>
}