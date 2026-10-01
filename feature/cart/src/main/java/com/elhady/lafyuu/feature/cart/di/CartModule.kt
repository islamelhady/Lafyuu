package com.elhady.lafyuu.feature.cart.di

import com.elhady.lafyuu.feature.cart.data.remote.CartApi
import com.elhady.lafyuu.feature.cart.data.repository.CartRepositoryImpl
import com.elhady.lafyuu.feature.cart.domain.repository.CartRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CartModule {

    @Binds
    @Singleton
    abstract fun bindCartRepository(cartRepositoryImpl: CartRepositoryImpl): CartRepository

    companion object {
        @Provides
        @Singleton
        fun provideCartApi(retrofit: Retrofit): CartApi {
            return retrofit.create(CartApi::class.java)
        }
    }
}