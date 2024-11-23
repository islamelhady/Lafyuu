package com.elhady.lafyuu.feature.checkout.data.mapper

import com.elhady.lafyuu.feature.checkout.data.remote.model.OrderCheckoutResponseDto
import com.elhady.lafyuu.feature.checkout.domain.model.CheckoutResult

fun OrderCheckoutResponseDto.toDomain(): CheckoutResult {
    val message = message.orEmpty()
    val paymentUrl = unifiedCheckoutUrl?.takeIf { it.isNotBlank() }
    return if (paymentUrl != null) {
        CheckoutResult.PaymentRequired(paymentUrl = paymentUrl, message = message)
    } else {
        CheckoutResult.Completed(message = message)
    }
}