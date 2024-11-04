package com.elhady.lafyuu.feature.orders.data.repository

import com.elhady.lafyuu.core.common.result.AppResult
import com.elhady.lafyuu.core.network.error.SafeApiCaller
import com.elhady.lafyuu.feature.orders.data.mapper.toDomain
import com.elhady.lafyuu.feature.orders.data.remote.OrdersApi
import com.elhady.lafyuu.feature.orders.domain.model.OrderHistory
import com.elhady.lafyuu.feature.orders.domain.model.OrderSummary
import com.elhady.lafyuu.feature.orders.domain.repository.OrdersRepository
import javax.inject.Inject

class OrdersRepositoryImpl @Inject constructor(
    private val ordersApi: OrdersApi,
    private val safeApiCaller: SafeApiCaller
) : OrdersRepository {

    override suspend fun getOrders(): AppResult<List<OrderSummary>> {
        return safeApiCaller.call { ordersApi.getOrders().orders.orEmpty().map { it.toDomain() } }
    }

    override suspend fun getOrderHistory(orderId: String): AppResult<OrderHistory> {
        return safeApiCaller.call {
            val historyDto = ordersApi.getOrderHistory(orderId)
            val ordersDto = ordersApi.getOrders()

            val matchingOrder = ordersDto.orders?.find { it.orderId == orderId }

            OrderHistory(
                orderId = historyDto.orderId ?: orderId,
                orderCode = historyDto.orderCode ?: matchingOrder?.orderCode ?: "",
                totalPrice = matchingOrder?.totalPrice ?: 0.0,
                paymentMethod = matchingOrder?.paymentMethod ?: "",
                createdAt = matchingOrder?.createdAt,
                updatedAt = matchingOrder?.updatedAt,
                history = historyDto.history.orEmpty().map { it.toDomain() }
            )
        }
    }
}
