package com.example.gramenkovtestproject.di

import com.example.gramenkovtestproject.data.network.PhotoService
import com.example.gramenkovtestproject.domain.utils.Keys.BASE_URL
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    @Provides
    fun getPhotoService(retrofit: Retrofit): PhotoService =
        retrofit.create(PhotoService::class.java)

}