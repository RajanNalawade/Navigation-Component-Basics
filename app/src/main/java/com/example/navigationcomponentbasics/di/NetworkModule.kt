package com.example.navigationcomponentbasics.di

import com.example.navigationcomponentbasics.api.UserApi
import com.example.navigationcomponentbasics.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.BASE_URL)
            .build()
    }

    @Singleton
    @Provides
    fun providesUserApi(retrofit: Retrofit): UserApi{
        return retrofit.create(UserApi::class.java)
    }

}