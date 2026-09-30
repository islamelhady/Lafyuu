package com.elhady.lafyuu.feature.home.di

import com.elhady.lafyuu.feature.home.data.remote.HomeApi
import com.elhady.lafyuu.feature.home.data.repository.HomeRepositoryImpl
import com.elhady.lafyuu.feature.home.domain.repository.HomeRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class HomeModule {

    @Binds
    @Singleton
    abstract fun bindHomeRepository(homeRepositoryImpl: HomeRepositoryImpl): HomeRepository

    companion object {
        @Provides
        @Singleton
        fun provideHomeApi(retrofit: Retrofit): HomeApi {
            return retrofit.create(HomeApi::class.java)
        }
    }
}
