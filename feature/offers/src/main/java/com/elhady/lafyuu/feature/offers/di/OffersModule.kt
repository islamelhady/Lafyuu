package com.elhady.lafyuu.feature.offers.di

import com.elhady.lafyuu.core.network.error.ApiErrorParser
import com.elhady.lafyuu.feature.offers.data.remote.OffersApi
import com.elhady.lafyuu.feature.offers.data.repository.OffersRepositoryImpl
import com.elhady.lafyuu.feature.offers.domain.repository.OffersRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OffersModule {

    @Provides
    @Singleton
    fun provideOffersApi(retrofit: Retrofit): OffersApi {
        return retrofit.create(OffersApi::class.java)
    }

    @Provides
    @Singleton
    fun provideOffersRepository(
        offersApi: OffersApi,
        apiErrorParser: ApiErrorParser
    ): OffersRepository {
        return OffersRepositoryImpl(offersApi, apiErrorParser)
    }
}
