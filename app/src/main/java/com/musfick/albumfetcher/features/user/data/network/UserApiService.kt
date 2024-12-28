package com.musfick.albumfetcher.features.user.data.network

import com.musfick.albumfetcher.features.user.data.network.dto.UserDto
import retrofit2.Response
import retrofit2.http.GET

interface UserApiService {
    @GET("users")
    suspend fun getUsers(): Response<List<UserDto>>
}