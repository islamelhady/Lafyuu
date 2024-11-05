package com.elhady.lafyuu.feature.search.di

import com.elhady.lafyuu.core.network.error.SafeApiCaller
import com.elhady.lafyuu.feature.search.data.remote.SearchApi
import com.elhady.lafyuu.feature.search.data.repository.SearchRepositoryImpl
import com.elhady.lafyuu.feature.search.domain.repository.SearchRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SearchModule {

    @Provides
    @Singleton
    fun provideSearchApi(retrofit: Retrofit): SearchApi {
        return retrofit.create(SearchApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSearchRepository(
        searchApi: SearchApi,
        safeApiCaller: SafeApiCaller
    ): SearchRepository {
        return SearchRepositoryImpl(searchApi, safeApiCaller)
    }
}
