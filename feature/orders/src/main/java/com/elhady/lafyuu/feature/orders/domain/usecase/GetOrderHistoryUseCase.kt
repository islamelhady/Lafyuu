package com.elhady.lafyuu.feature.orders.domain.usecase

import com.elhady.lafyuu.core.common.result.AppResult
import com.elhady.lafyuu.feature.orders.domain.model.OrderHistory
import com.elhady.lafyuu.feature.orders.domain.repository.OrdersRepository
import javax.inject.Inject

class GetOrderHistoryUseCase @Inject constructor(
    private val repository: OrdersRepository
) {
    suspend operator fun invoke(orderId: String): AppResult<OrderHistory> {
        return repository.getOrderHistory(orderId)
    }
}
