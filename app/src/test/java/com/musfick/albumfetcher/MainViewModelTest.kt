package com.musfick.albumfetcher

import app.cash.turbine.test
import com.musfick.albumfetcher.core.utils.Resource
import com.musfick.albumfetcher.features.album.data.network.AlbumApiService
import com.musfick.albumfetcher.features.album.domain.repository.AlbumRepository
import com.musfick.albumfetcher.features.album.domain.repository.AlbumRepositoryImpl
import com.musfick.albumfetcher.features.fetcher.domain.service.AlbumFetcherService
import com.musfick.albumfetcher.features.fetcher.utils.FetchServiceAction
import com.musfick.albumfetcher.features.photo.data.network.PhotoApiService
import com.musfick.albumfetcher.features.photo.domain.repository.PhotoRepository
import com.musfick.albumfetcher.features.photo.domain.repository.PhotoRepositoryImpl
import com.musfick.albumfetcher.features.user.data.network.UserApiService
import com.musfick.albumfetcher.features.user.domain.repository.UserRepository
import com.musfick.albumfetcher.features.user.domain.repository.UserRepositoryImpl
import com.musfick.albumfetcher.utils.MockWebServerDispatcher
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

class MainViewModelTest {

    lateinit var viewModel: MainViewModel
    lateinit var fetcherService: AlbumFetcherService

    private val mockWebServer = MockWebServer()
    private val client = OkHttpClient.Builder().build()
    private lateinit var albumRepository: AlbumRepository
    private lateinit var photoRepository: PhotoRepository
    private lateinit var userRepository: UserRepository
    private val webServerDispatcher = MockWebServerDispatcher()

    @Before
    fun setup(){
        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val albumApiService = retrofit.create(AlbumApiService::class.java)
        albumRepository = AlbumRepositoryImpl(albumApiService)

        val photoApiService = retrofit.create(PhotoApiService::class.java)
        photoRepository = PhotoRepositoryImpl(photoApiService)

        val userApiService = retrofit.create(UserApiService::class.java)
        userRepository = UserRepositoryImpl(userApiService)

        viewModel = MainViewModel()
        fetcherService = AlbumFetcherService()
        fetcherService.setDependencies(
            albumRepository = albumRepository,
            photoRepository = photoRepository,
            userRepository = userRepository
        )
    }

    @Test
    fun check_album_fetcher_assigned_successfully() = runTest {
        viewModel.albumFetchService.value = fetcherService
        viewModel.isFetchServiceBound = true
        viewModel.albumFetchService.test {
            assertNotNull(awaitItem())
            assertTrue(viewModel.isFetchServiceBound)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun check_fetch_start_and_ui_state_loading() = runTest {
        viewModel.albumFetchService.value = fetcherService
        viewModel.isFetchServiceBound = true
        viewModel.uiState.test {
            viewModel.albumFetchService.value?.executeAction(FetchServiceAction.Start)
            assertIs<MainUiState.Loading>(awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun check_fetch_start_and_server_error_and_ui_state_error() = runTest {
        mockWebServer.dispatcher = webServerDispatcher.ErrorDispatcher()
        viewModel.albumFetchService.value = fetcherService
        viewModel.isFetchServiceBound = true
        viewModel.uiState.test {
            viewModel.albumFetchService.value?.executeAction(FetchServiceAction.Start)
            assertIs<MainUiState.Loading>(awaitItem())
            assertIs<MainUiState.Error>(awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun check_fetch_start_and_stop_and_ui_state_error() = runTest {
        mockWebServer.dispatcher = webServerDispatcher.RequestDispatcher()
        viewModel.albumFetchService.value = fetcherService
        viewModel.isFetchServiceBound = true
        viewModel.uiState.test {
            viewModel.albumFetchService.value?.executeAction(FetchServiceAction.Start)
            assertIs<MainUiState.Loading>(awaitItem())
            viewModel.albumFetchService.value?.executeAction(FetchServiceAction.Stop)
            assertIs<MainUiState.Error>(awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun check_fetch_start_and_ui_state_success() = runTest {
        mockWebServer.dispatcher = webServerDispatcher.RequestDispatcher()
        viewModel.albumFetchService.value = fetcherService
        viewModel.isFetchServiceBound = true
        viewModel.uiState.test {
            viewModel.albumFetchService.value?.executeAction(FetchServiceAction.Start)
            assertIs<MainUiState.Loading>(awaitItem())
            assertIs<MainUiState.Success>(awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun check_fetch_retry_on_error() = runTest {
        mockWebServer.dispatcher = webServerDispatcher.ErrorDispatcher()
        viewModel.albumFetchService.value = fetcherService
        viewModel.isFetchServiceBound = true
        viewModel.uiState.test {
            viewModel.albumFetchService.value?.executeAction(FetchServiceAction.Start)
            assertIs<MainUiState.Loading>(awaitItem())
            assertIs<MainUiState.Error>(awaitItem())
            mockWebServer.dispatcher = webServerDispatcher.RequestDispatcher()
            viewModel.onRetry()
            assertIs<MainUiState.Loading>(awaitItem())
            assertIs<MainUiState.Success>(awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

}