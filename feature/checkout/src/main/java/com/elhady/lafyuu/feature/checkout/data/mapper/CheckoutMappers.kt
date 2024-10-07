package com.elhady.lafyuu.feature.checkout.data.mapper

import com.elhady.lafyuu.feature.checkout.data.remote.model.OrderCheckoutResponseDto
import com.elhady.lafyuu.feature.checkout.domain.model.CheckoutResult

fun OrderCheckoutResponseDto.toDomain(): CheckoutResult {
    return CheckoutResult(
        message = message.orEmpty(),
        unifiedCheckoutUrl = unifiedCheckoutUrl,
        paymentClientSecret = paymentClientSecret
    )
}
