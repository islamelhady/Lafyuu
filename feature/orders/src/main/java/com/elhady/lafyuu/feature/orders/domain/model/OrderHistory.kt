package com.elhady.lafyuu.feature.orders.domain.model

data class OrderHistory(
    val orderId: String,
    val orderCode: String,
    val history: List<OrderHistoryEntry>
)

data class OrderHistoryEntry(
    val status: String,
    val changeDate: String?,
    val notes: String?
)
