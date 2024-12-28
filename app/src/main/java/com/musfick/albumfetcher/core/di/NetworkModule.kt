package com.musfick.albumfetcher.core.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.musfick.albumfetcher.BuildConfig
import com.musfick.albumfetcher.core.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor? {
        if (!BuildConfig.DEBUG) return null
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor?,
        @ApplicationContext app: Context,
    ) = OkHttpClient.Builder().apply {
        if (httpLoggingInterceptor != null) {
            addInterceptor(httpLoggingInterceptor)
        }
    }.also {
        it.addInterceptor(ChuckerInterceptor.Builder(app).build())
        it.connectTimeout(Constants.NETWORK_TIMEOUT, TimeUnit.SECONDS)
        it.readTimeout(Constants.NETWORK_TIMEOUT, TimeUnit.SECONDS)
        it.writeTimeout(Constants.NETWORK_TIMEOUT, TimeUnit.SECONDS)
    }.build()

    @Provides
    @Singleton
    fun providesRetrofitClient(
        client: OkHttpClient
    ): Retrofit = Retrofit.Builder().also {
        it.baseUrl(Constants.BASE_URL)
        it.client(client)
        it.addConverterFactory(GsonConverterFactory.create())
    }.build()

}