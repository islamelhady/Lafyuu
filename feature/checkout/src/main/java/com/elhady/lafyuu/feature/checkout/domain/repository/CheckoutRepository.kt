package com.elhady.lafyuu.feature.checkout.domain.repository

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.checkout.domain.model.Address
import com.elhady.lafyuu.feature.checkout.domain.model.CheckoutResult

interface CheckoutRepository {
    suspend fun getAddresses(): AppResult<List<Address>>
    suspend fun createAddress(
        state: String,
        city: String,
        street: String,
        apartment: String,
        phoneNumber: String,
        notes: String
    ): AppResult<Address>
    suspend fun deleteAddress(addressId: String): AppResult<Unit>
    suspend fun checkout(
        shippingAddressId: String,
        paymentMethod: String,
        couponCode: String?
    ): AppResult<CheckoutResult>
}
