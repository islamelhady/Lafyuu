package com.elhady.lafyuu.feature.checkout.domain.repository

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.checkout.domain.model.CheckoutResult
import com.elhady.lafyuu.feature.checkout.domain.model.PaymentMethod

interface CheckoutRepository {
    suspend fun checkout(
        shippingAddressId: String,
        paymentMethod: PaymentMethod,
        couponCode: String?
    ): AppResult<CheckoutResult>
}