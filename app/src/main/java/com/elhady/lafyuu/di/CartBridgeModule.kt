package com.elhady.lafyuu.di

import com.elhady.lafyuu.CartFavoritesGatewayImpl
import com.elhady.lafyuu.feature.cart.domain.repository.CartFavoritesGateway
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class CartBridgeModule {
    @Binds
    abstract fun bindCartFavoritesGateway(impl: CartFavoritesGatewayImpl): CartFavoritesGateway
}