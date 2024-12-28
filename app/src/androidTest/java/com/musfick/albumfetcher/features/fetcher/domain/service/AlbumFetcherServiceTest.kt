package com.musfick.albumfetcher.features.fetcher.domain.service

import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ServiceTestRule
import app.cash.turbine.test
import com.musfick.albumfetcher.features.album.domain.repository.AlbumRepository
import com.musfick.albumfetcher.features.fetcher.utils.FetchServiceAction
import com.musfick.albumfetcher.features.fetcher.utils.FetchState
import com.musfick.albumfetcher.features.photo.domain.repository.PhotoRepository
import com.musfick.albumfetcher.features.user.domain.repository.UserRepository
import com.musfick.albumfetcher.utils.MockWebServerDispatcher
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class AlbumFetcherServiceTest {

    @get:Rule
    val serviceRule = ServiceTestRule()

    @get:Rule
    val hiltRule = HiltAndroidRule(this)
    private val mockWebServerDispatcher = MockWebServerDispatcher()

    @Inject
    lateinit var mockWebServer: MockWebServer
    @Inject
    lateinit var albumRepository: AlbumRepository
    @Inject
    lateinit var photoRepository: PhotoRepository
    @Inject
    lateinit var userRepository: UserRepository


    @Before
    fun setup(){
        hiltRule.inject()
    }

    @Test
    fun test_bind_service() = runTest{
        mockWebServer.dispatcher = mockWebServerDispatcher.RequestDispatcher()

        val serviceIntent = Intent(
            ApplicationProvider.getApplicationContext<Context>(),
            AlbumFetcherService::class.java
        )
        val binder: IBinder = serviceRule.bindService(serviceIntent)
        val service: AlbumFetcherService = (binder as AlbumFetcherService.AlbumFetcherServiceBinder).getService()
        service.setDependencies(
            albumRepository, photoRepository, userRepository
        )

        service.executeAction(FetchServiceAction.Start)
        service.fetchState.test {
            assertEquals(awaitItem() , FetchState.Loading)
            cancelAndIgnoreRemainingEvents()
        }

    }

    @Test
    fun test_service_start_fetch_get_success() = runTest{
        mockWebServer.dispatcher = mockWebServerDispatcher.RequestDispatcher()

        val serviceIntent = Intent(
            ApplicationProvider.getApplicationContext<Context>(),
            AlbumFetcherService::class.java
        )
        val binder: IBinder = serviceRule.bindService(serviceIntent)
        val service: AlbumFetcherService = (binder as AlbumFetcherService.AlbumFetcherServiceBinder).getService()
        service.setDependencies(
            albumRepository, photoRepository, userRepository
        )

        service.executeAction(FetchServiceAction.Start)
        service.fetchState.test {
            assertEquals(awaitItem() , FetchState.Loading)
            assertTrue(awaitItem() is FetchState.Success)
            cancelAndIgnoreRemainingEvents()
        }

    }

    @Test
    fun test_service_stop_fetch_get_exception() = runTest{
        mockWebServer.dispatcher = mockWebServerDispatcher.RequestDispatcher()

        val serviceIntent = Intent(
            ApplicationProvider.getApplicationContext<Context>(),
            AlbumFetcherService::class.java
        )
        val binder: IBinder = serviceRule.bindService(serviceIntent)
        val service: AlbumFetcherService = (binder as AlbumFetcherService.AlbumFetcherServiceBinder).getService()
        service.setDependencies(
            albumRepository, photoRepository, userRepository
        )

        service.executeAction(FetchServiceAction.Start)
        service.fetchState.test {
            assertEquals(awaitItem() , FetchState.Loading)
            service.executeAction(FetchServiceAction.Stop)
            assertTrue(awaitItem() is FetchState.Error)
            cancelAndIgnoreRemainingEvents()
        }

    }

    @Test
    fun test_service_fetch_exception_catch() = runTest{
        mockWebServer.dispatcher = mockWebServerDispatcher.ErrorDispatcher()

        val serviceIntent = Intent(
            ApplicationProvider.getApplicationContext<Context>(),
            AlbumFetcherService::class.java
        )
        val binder: IBinder = serviceRule.bindService(serviceIntent)
        val service: AlbumFetcherService = (binder as AlbumFetcherService.AlbumFetcherServiceBinder).getService()
        service.setDependencies(
            albumRepository, photoRepository, userRepository
        )

        service.executeAction(FetchServiceAction.Start)
        service.fetchState.test {
            assertEquals(awaitItem() , FetchState.Loading)
            service.executeAction(FetchServiceAction.Stop)
            assertTrue(awaitItem() is FetchState.Error)
            cancelAndIgnoreRemainingEvents()
        }

    }
}