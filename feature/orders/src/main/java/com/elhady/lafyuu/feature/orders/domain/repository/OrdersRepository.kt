package com.elhady.lafyuu.feature.orders.domain.repository

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.orders.domain.model.OrderHistory
import com.elhady.lafyuu.feature.orders.domain.model.OrderSummary

interface OrdersRepository {
    suspend fun getOrders(): AppResult<List<OrderSummary>>
    suspend fun getOrderHistory(orderId: String): AppResult<OrderHistory>
}
