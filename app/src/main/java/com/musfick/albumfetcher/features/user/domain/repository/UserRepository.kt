package com.musfick.albumfetcher.features.user.domain.repository
import com.musfick.albumfetcher.features.user.domain.model.User

interface UserRepository {
    suspend fun getUsers():Result<List<User>>
}