package com.musfick.albumfetcher.features.photo.di
import com.musfick.albumfetcher.features.photo.data.network.PhotoApiService
import com.musfick.albumfetcher.features.photo.domain.repository.PhotoRepository
import com.musfick.albumfetcher.features.photo.domain.repository.PhotoRepositoryImpl
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
    replaces = [PhotoModule::class]
)

interface FakePhotoModule {

    @Binds
    @Singleton
    fun providePhotoRepositoryImpl(repository: PhotoRepositoryImpl): PhotoRepository

    companion object {
        @Provides
        @Singleton
        fun providePhotoApiService(retrofit: Retrofit): PhotoApiService = retrofit.create(
            PhotoApiService::class.java)
    }
}