package com.elhady.lafyuu.feature.orders.data.repository

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.network.error.ApiErrorParser
import com.elhady.lafyuu.core.network.error.NetworkError
import com.elhady.lafyuu.feature.orders.data.mapper.toDomain
import com.elhady.lafyuu.feature.orders.data.remote.OrdersApi
import com.elhady.lafyuu.feature.orders.domain.model.OrderHistory
import com.elhady.lafyuu.feature.orders.domain.model.OrderSummary
import com.elhady.lafyuu.feature.orders.domain.repository.OrdersRepository
import javax.inject.Inject

class OrdersRepositoryImpl @Inject constructor(
    private val ordersApi: OrdersApi,
    private val apiErrorParser: ApiErrorParser
) : OrdersRepository {

    override suspend fun getOrders(): AppResult<List<OrderSummary>> {
        return try {
            val response = ordersApi.getOrders()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    AppResult.Success(body.orders.orEmpty().map { it.toDomain() })
                } else {
                    AppResult.Success(emptyList())
                }
            } else {
                mapError(apiErrorParser.parseError(response))
            }
        } catch (e: Exception) {
            mapError(apiErrorParser.parseException(e))
        }
    }

    override suspend fun getOrderHistory(orderId: String): AppResult<OrderHistory> {
        return try {
            val response = ordersApi.getOrderHistory(orderId)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    AppResult.Success(
                        OrderHistory(
                            orderId = body.orderId ?: orderId,
                            orderCode = body.orderCode ?: "",
                            history = body.history.orEmpty().map { it.toDomain() }
                        )
                    )
                } else {
                    AppResult.Error("Order history not found")
                }
            } else {
                mapError(apiErrorParser.parseError(response))
            }
        } catch (e: Exception) {
            mapError(apiErrorParser.parseException(e))
        }
    }

    private fun mapError(networkError: NetworkError): AppResult.Error {
        return when (networkError) {
            is NetworkError.ApiError -> {
                val details = networkError.details
                val errorMessage = details.detail
                    ?: details.title
                    ?: details.errors?.values?.flatten()?.firstOrNull()
                    ?: "Request failed"
                AppResult.Error(message = errorMessage)
            }
            is NetworkError.Connectivity -> AppResult.Error(message = "No internet connection")
            is NetworkError.Serialization -> AppResult.Error(message = "Server error (Parsing)")
            is NetworkError.Server -> AppResult.Error(message = "Internal server error")
            is NetworkError.Unknown -> AppResult.Error(
                message = networkError.throwable.localizedMessage ?: "An unexpected error occurred",
                throwable = networkError.throwable
            )
        }
    }
}
