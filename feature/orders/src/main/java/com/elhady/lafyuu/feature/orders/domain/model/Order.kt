package com.elhady.lafyuu.feature.orders.domain.model

data class OrderSummary(
    val orderId: String,
    val orderCode: String,
    val createdAt: String,
    val updatedAt: String?,
    val status: String,
    val totalPrice: Double,
    val paymentMethod: String
)
