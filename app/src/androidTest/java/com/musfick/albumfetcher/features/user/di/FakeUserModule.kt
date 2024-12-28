package com.musfick.albumfetcher.features.user.di
import com.musfick.albumfetcher.features.user.data.network.UserApiService
import com.musfick.albumfetcher.features.user.domain.repository.UserRepository
import com.musfick.albumfetcher.features.user.domain.repository.UserRepositoryImpl
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
    replaces = [UserModule::class]
)

interface FakeUserModule {

    @Binds
    @Singleton
    fun provideUserRepositoryImpl(repository: UserRepositoryImpl): UserRepository

    companion object {
        @Provides
        @Singleton
        fun provideUserApiService(retrofit: Retrofit): UserApiService = retrofit.create(
            UserApiService::class.java)
    }
}