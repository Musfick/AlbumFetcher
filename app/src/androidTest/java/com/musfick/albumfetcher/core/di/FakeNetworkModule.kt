package com.musfick.albumfetcher.core.di

import com.musfick.albumfetcher.utils.MockWebServerDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [NetworkModule::class]
)
object FakeNetworkModule {

    @Provides
    @Singleton
    fun provideMockWebServer() = MockWebServer()

    @Provides
    @Singleton
    fun provideOkHttpClient() = OkHttpClient.Builder().build()

    @Provides
    @Singleton
    fun provideMockWebServerDispatcher() = MockWebServerDispatcher()

    @Singleton
    @Provides
    fun provideRetrofit(
        mockWebServer: MockWebServer,
        client: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }




}