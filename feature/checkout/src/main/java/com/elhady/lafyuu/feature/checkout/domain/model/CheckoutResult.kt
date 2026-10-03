package com.elhady.lafyuu.feature.checkout.domain.model

data class CheckoutResult(
    val message: String,
    val unifiedCheckoutUrl: String?,
    val paymentClientSecret: String?
)
