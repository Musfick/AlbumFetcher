package com.musfick.albumfetcher.features.album.domain.repository

import com.musfick.albumfetcher.core.utils.Constants
import com.musfick.albumfetcher.features.album.data.network.AlbumApiService
import com.musfick.albumfetcher.utils.MockWebServerDispatcher
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AlbumRepositoryImplTest {
    private val mockWebServer = MockWebServer()
    private val client = OkHttpClient.Builder().build()
    private lateinit var albumRepository: AlbumRepository
    private val webServerDispatcher = MockWebServerDispatcher()

    @Before
    fun setup() {
        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val albumApiService = retrofit.create(AlbumApiService::class.java)
        albumRepository = AlbumRepositoryImpl(albumApiService)
    }

    @Test
    fun test_getAlbums_returns_failure_for_unsuccessful_response() = runTest {
        mockWebServer.dispatcher = webServerDispatcher.ErrorDispatcher()
        val result = albumRepository.getAlbums()
        assertTrue(result.isFailure)
        assertEquals(Constants.ERROR_OCCURRED, result.exceptionOrNull()?.message)
    }

    @Test
    fun test_getAlbums_returns_failure_for_empty_response() = runTest {
        mockWebServer.dispatcher = webServerDispatcher.EmptyDispatcher()
        val result = albumRepository.getAlbums()
        assertTrue(result.isFailure)
        assertEquals(Constants.ERROR_NO_ITEM_FOUND, result.exceptionOrNull()?.message)
    }


    @Test
    fun test_getAlbums_returns_success_from_server() = runTest {
        mockWebServer.dispatcher = webServerDispatcher.RequestDispatcher()
        val result = albumRepository.getAlbums()

        assertTrue(result.isSuccess)
        assertNotNull(result.getOrNull())
        assertNotEquals(0, result.getOrNull()?.size)
    }

    @Test
    fun test_getAlbums_handles_exceptions() = runTest {
        mockWebServer.shutdown()
        val result = albumRepository.getAlbums()
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is Exception)
    }

}