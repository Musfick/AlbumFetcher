package com.musfick.albumfetcher.features.photo.di
import com.musfick.albumfetcher.features.photo.domain.repository.PhotoRepositoryImpl
import com.musfick.albumfetcher.features.photo.data.network.PhotoApiService
import com.musfick.albumfetcher.features.photo.domain.repository.PhotoRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface PhotoModule {

    @Binds
    @Singleton
    fun providePhotoRepositoryImpl(repository: PhotoRepositoryImpl): PhotoRepository


    companion object {
        @Provides
        @Singleton
        fun providePhotoApiService(retrofit: Retrofit): PhotoApiService = retrofit.create(PhotoApiService::class.java)
    }
}