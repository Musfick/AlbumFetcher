package com.musfick.albumfetcher.features.user.domain.repository

import com.musfick.albumfetcher.core.utils.Constants
import com.musfick.albumfetcher.features.user.data.network.UserApiService
import com.musfick.albumfetcher.utils.MockWebServerDispatcher
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UserRepositoryImplTest {
    private val mockWebServer = MockWebServer()
    private val client = OkHttpClient.Builder().build()
    private lateinit var userRepository: UserRepository
    private val webServerDispatcher = MockWebServerDispatcher()

    @Before
    fun setup() {
        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val userApiService = retrofit.create(UserApiService::class.java)
        userRepository = UserRepositoryImpl(userApiService)
    }

    @Test
    fun test_get_users_returns_failure_for_unsuccessful_response() = runTest {
        mockWebServer.dispatcher = webServerDispatcher.ErrorDispatcher()
        val result = userRepository.getUsers()
        assertTrue(result.isFailure)
        assertEquals(Constants.ERROR_OCCURRED, result.exceptionOrNull()?.message)
    }

    @Test
    fun test_get_users_returns_failure_for_empty_response() = runTest {
        mockWebServer.dispatcher = webServerDispatcher.EmptyDispatcher()
        val result = userRepository.getUsers()
        assertTrue(result.isFailure)
        assertEquals(Constants.ERROR_NO_ITEM_FOUND, result.exceptionOrNull()?.message)
    }


    @Test
    fun test_get_users_returns_success_from_server() = runTest {
        mockWebServer.dispatcher = webServerDispatcher.RequestDispatcher()
        val result = userRepository.getUsers()

        assertTrue(result.isSuccess)
        assertNotNull(result.getOrNull())
        assertNotEquals(result.getOrNull()?.size, 0)
    }

    @Test
    fun test_get_users_handles_exceptions() = runTest {
        mockWebServer.shutdown()
        val result = userRepository.getUsers()
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is Exception)
    }

}