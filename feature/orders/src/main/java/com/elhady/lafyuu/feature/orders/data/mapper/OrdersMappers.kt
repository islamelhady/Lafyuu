package com.elhady.lafyuu.feature.orders.data.mapper

import com.elhady.lafyuu.feature.orders.data.remote.model.GetOrderEntryDto
import com.elhady.lafyuu.feature.orders.data.remote.model.OrderHistoryEntryDto
import com.elhady.lafyuu.feature.orders.domain.model.OrderHistoryEntry
import com.elhady.lafyuu.feature.orders.domain.model.OrderSummary

fun GetOrderEntryDto.toDomain(): OrderSummary {
    return OrderSummary(
        orderId = orderId,
        orderCode = orderCode,
        createdAt = createdAt ?: "",
        updatedAt = updatedAt,
        status = status,
        totalPrice = totalPrice,
        paymentMethod = paymentMethod
    )
}

fun OrderHistoryEntryDto.toDomain(): OrderHistoryEntry {
    return OrderHistoryEntry(
        status = orderStatus,
        changeDate = changeDate,
        notes = notes
    )
}
