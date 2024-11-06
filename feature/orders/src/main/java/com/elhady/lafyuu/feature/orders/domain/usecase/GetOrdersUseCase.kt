package com.elhady.lafyuu.feature.orders.domain.usecase

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.orders.domain.model.OrderSummary
import com.elhady.lafyuu.feature.orders.domain.repository.OrdersRepository
import javax.inject.Inject

class GetOrdersUseCase @Inject constructor(
    private val repository: OrdersRepository
) {
    suspend operator fun invoke(): AppResult<List<OrderSummary>> {
        return repository.getOrders()
    }
}
