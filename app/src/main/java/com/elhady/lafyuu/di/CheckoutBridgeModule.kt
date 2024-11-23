package com.elhady.lafyuu.di

import com.elhady.lafyuu.CheckoutAddressGatewayImpl
import com.elhady.lafyuu.CheckoutCartGatewayImpl
import com.elhady.lafyuu.feature.checkout.domain.repository.CheckoutAddressGateway
import com.elhady.lafyuu.feature.checkout.domain.repository.CheckoutCartGateway
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class CheckoutBridgeModule {
    @Binds
    abstract fun bindCheckoutAddressGateway(impl: CheckoutAddressGatewayImpl): CheckoutAddressGateway

    @Binds
    abstract fun bindCheckoutCartGateway(impl: CheckoutCartGatewayImpl): CheckoutCartGateway
}