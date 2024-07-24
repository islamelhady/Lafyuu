package com.elhady.lafyuu.feature.checkout.di

import com.elhady.lafyuu.feature.checkout.data.remote.CheckoutApi
import com.elhady.lafyuu.feature.checkout.data.repository.CheckoutRepositoryImpl
import com.elhady.lafyuu.feature.checkout.domain.repository.CheckoutRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CheckoutModule {

    @Binds
    @Singleton
    abstract fun bindCheckoutRepository(checkoutRepositoryImpl: CheckoutRepositoryImpl): CheckoutRepository

    companion object {
        @Provides
        @Singleton
        fun provideCheckoutApi(retrofit: Retrofit): CheckoutApi {
            return retrofit.create(CheckoutApi::class.java)
        }
    }
}
