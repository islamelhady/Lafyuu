package com.elhady.lafyuu.feature.review.di

import com.elhady.lafyuu.core.network.error.SafeApiCaller
import com.elhady.lafyuu.feature.review.data.remote.ReviewApi
import com.elhady.lafyuu.feature.review.data.repository.ReviewRepositoryImpl
import com.elhady.lafyuu.feature.review.domain.repository.ReviewRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ReviewModule {

    @Provides
    @Singleton
    fun provideReviewApi(retrofit: Retrofit): ReviewApi {
        return retrofit.create(ReviewApi::class.java)
    }

    @Provides
    @Singleton
    fun provideReviewRepository(
        reviewApi: ReviewApi,
        safeApiCaller: SafeApiCaller
    ): ReviewRepository {
        return ReviewRepositoryImpl(reviewApi, safeApiCaller)
    }
}
