package com.musfick.albumfetcher.features.photo.domain.repository

import com.musfick.albumfetcher.core.utils.Constants
import com.musfick.albumfetcher.features.photo.data.network.PhotoApiService
import com.musfick.albumfetcher.utils.MockWebServerDispatcher
import org.junit.Assert.*
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PhotoRepositoryImplTest {

    private val mockWebServer = MockWebServer()
    private val client = OkHttpClient.Builder().build()
    private lateinit var photoRepository: PhotoRepository
    private val webServerDispatcher = MockWebServerDispatcher()

    @Before
    fun setup() {
        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val photoApiService = retrofit.create(PhotoApiService::class.java)
        photoRepository = PhotoRepositoryImpl(photoApiService)
    }

    @Test
    fun test_get_photos_returns_failure_for_unsuccessful_response() = runTest {
        mockWebServer.dispatcher = webServerDispatcher.ErrorDispatcher()
        val result = photoRepository.getPhotos()
        assertTrue(result.isFailure)
        assertEquals(Constants.ERROR_OCCURRED, result.exceptionOrNull()?.message)
    }

    @Test
    fun test_get_photos_returns_failure_for_empty_response() = runTest {
        mockWebServer.dispatcher = webServerDispatcher.EmptyDispatcher()
        val result = photoRepository.getPhotos()
        assertTrue(result.isFailure)
        assertEquals(Constants.ERROR_NO_ITEM_FOUND, result.exceptionOrNull()?.message)
    }


    @Test
    fun test_get_photos_returns_success_from_server() = runTest {
        mockWebServer.dispatcher = webServerDispatcher.RequestDispatcher()
        val result = photoRepository.getPhotos()

        assertTrue(result.isSuccess)
        assertNotNull(result.getOrNull())
        assertNotEquals(result.getOrNull()?.size, 0)
    }

    @Test
    fun test_get_photos_handles_exceptions() = runTest {
        mockWebServer.shutdown()
        val result = photoRepository.getPhotos()
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is Exception)
    }
}