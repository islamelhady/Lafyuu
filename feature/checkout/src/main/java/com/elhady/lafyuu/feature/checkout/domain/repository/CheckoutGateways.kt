package com.elhady.lafyuu.feature.checkout.domain.repository

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.checkout.domain.model.OrderSummary
import com.elhady.lafyuu.feature.checkout.domain.model.ShippingAddress

interface CheckoutAddressGateway {
    suspend fun getAddresses(): AppResult<List<ShippingAddress>>
    suspend fun deleteAddress(addressId: String): AppResult<Unit>
}

interface CheckoutCartGateway {
    suspend fun getOrderSummary(): AppResult<OrderSummary>

    suspend fun refreshCart()
}