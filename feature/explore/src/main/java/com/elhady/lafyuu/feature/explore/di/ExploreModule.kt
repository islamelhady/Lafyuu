package com.elhady.lafyuu.feature.explore.di

import com.elhady.lafyuu.core.network.error.ApiErrorParser
import com.elhady.lafyuu.feature.explore.data.remote.ExploreApi
import com.elhady.lafyuu.feature.explore.data.repository.ExploreRepositoryImpl
import com.elhady.lafyuu.feature.explore.domain.repository.ExploreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ExploreModule {

    @Provides
    @Singleton
    fun provideExploreApi(retrofit: Retrofit): ExploreApi {
        return retrofit.create(ExploreApi::class.java)
    }

    @Provides
    @Singleton
    fun provideExploreRepository(
        exploreApi: ExploreApi,
        apiErrorParser: ApiErrorParser
    ): ExploreRepository {
        return ExploreRepositoryImpl(exploreApi, apiErrorParser)
    }
}
