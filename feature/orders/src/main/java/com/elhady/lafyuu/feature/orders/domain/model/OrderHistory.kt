package com.elhady.lafyuu.feature.orders.domain.model

data class OrderHistory(
    val orderId: String,
    val orderCode: String,
    val totalPrice: Double = 0.0,
    val paymentMethod: String = "",
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val history: List<OrderHistoryEntry> = emptyList()
)

data class OrderHistoryEntry(
    val status: String,
    val changeDate: String?,
    val notes: String?
)
