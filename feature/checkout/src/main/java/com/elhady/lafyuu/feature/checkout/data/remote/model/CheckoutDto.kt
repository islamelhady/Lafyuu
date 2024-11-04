package com.elhady.lafyuu.feature.checkout.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class OrderCheckoutRequestDto(
    val shippingAddressId: String,
    val paymentMethod: String,
    val couponCode: String? = null
)

@Serializable
data class OrderCheckoutResponseDto(
    val message: String? = null,
    val unifiedCheckoutUrl: String? = null,
    val paymentClientSecret: String? = null
)
