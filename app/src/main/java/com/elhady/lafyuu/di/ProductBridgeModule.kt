package com.elhady.lafyuu.di

import com.elhady.lafyuu.ProductCartGatewayImpl
import com.elhady.lafyuu.ProductFavoriteGatewayImpl
import com.elhady.lafyuu.feature.product.domain.repository.ProductCartGateway
import com.elhady.lafyuu.feature.product.domain.repository.ProductFavoriteGateway
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ProductBridgeModule {
    @Binds
    abstract fun bindCartGateway(productCartGatewayImpl: ProductCartGatewayImpl): ProductCartGateway

    @Binds
    abstract fun bindFavoriteGateway(productFavoriteGatewayImpl: ProductFavoriteGatewayImpl): ProductFavoriteGateway
}