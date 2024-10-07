package com.elhady.lafyuu.feature.checkout.domain.repository

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.checkout.domain.model.CheckoutResult

interface CheckoutRepository {
    suspend fun checkout(
        shippingAddressId: String,
        paymentMethod: String,
        couponCode: String?
    ): AppResult<CheckoutResult>
}
