package com.musfick.albumfetcher.features.album.di
import com.musfick.albumfetcher.features.album.data.network.AlbumApiService
import com.musfick.albumfetcher.features.album.domain.repository.AlbumRepository
import com.musfick.albumfetcher.features.album.domain.repository.AlbumRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AlbumModule::class]
)

interface FakeAlbumModule {

    @Binds
    @Singleton
    fun provideAlbumRepositoryImpl(repository: AlbumRepositoryImpl): AlbumRepository

    companion object {
        @Provides
        @Singleton
        fun provideAlbumApiService(retrofit: Retrofit): AlbumApiService = retrofit.create(
            AlbumApiService::class.java)
    }
}