package com.elhady.lafyuu.feature.orders.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class GetAllOrdersResponseDto(
    val message: String,
    val orders: List<GetOrderEntryDto>? = null
)

@Serializable
data class GetOrderEntryDto(
    val orderId: String,
    val orderCode: String,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val status: String,
    val totalPrice: Double,
    val paymentMethod: String
)

@Serializable
data class GetOrdersHistoryResponseDto(
    val message: String,
    val orderId: String? = null,
    val orderCode: String? = null,
    val history: List<OrderHistoryEntryDto>? = null
)

@Serializable
data class OrderHistoryEntryDto(
    val orderStatus: String,
    val changeDate: String? = null,
    val notes: String? = null
)
