package com.elhady.lafyuu.feature.orders.data.repository

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.network.error.ApiErrorParser
import com.elhady.lafyuu.core.network.error.NetworkError
import com.elhady.lafyuu.core.network.error.toErrorMessage
import com.elhady.lafyuu.feature.orders.data.mapper.toDomain
import com.elhady.lafyuu.feature.orders.data.remote.OrdersApi
import com.elhady.lafyuu.feature.orders.domain.model.OrderHistory
import com.elhady.lafyuu.feature.orders.domain.model.OrderSummary
import com.elhady.lafyuu.feature.orders.domain.repository.OrdersRepository
import kotlinx.coroutines.CancellationException
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
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            mapError(apiErrorParser.parseException(e))
        }
    }

    override suspend fun getOrderHistory(orderId: String): AppResult<OrderHistory> {
        return try {
            val historyResponse = ordersApi.getOrderHistory(orderId)
            val ordersResponse = ordersApi.getOrders()

            if (historyResponse.isSuccessful) {
                val body = historyResponse.body()
                if (body != null) {
                    val matchingOrder = if (ordersResponse.isSuccessful) {
                        ordersResponse.body()?.orders?.find { it.orderId == orderId }
                    } else null

                    AppResult.Success(
                        OrderHistory(
                            orderId = body.orderId ?: orderId,
                            orderCode = body.orderCode ?: matchingOrder?.orderCode ?: "",
                            totalPrice = matchingOrder?.totalPrice ?: 0.0,
                            paymentMethod = matchingOrder?.paymentMethod ?: "",
                            createdAt = matchingOrder?.createdAt,
                            updatedAt = matchingOrder?.updatedAt,
                            history = body.history.orEmpty().map { it.toDomain() }
                        )
                    )
                } else {
                    AppResult.Error("Order history not found")
                }
            } else {
                mapError(apiErrorParser.parseError(historyResponse))
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            mapError(apiErrorParser.parseException(e))
        }
    }

    private fun mapError(networkError: NetworkError): AppResult.Error {
        return when (networkError) {
            is NetworkError.ApiError -> AppResult.Error(
                message = networkError.toErrorMessage(),
                status = networkError.details.status,
                validationErrors = networkError.details.errors
            )
            is NetworkError.Connectivity -> AppResult.Error(message = networkError.toErrorMessage())
            is NetworkError.Serialization -> AppResult.Error(message = networkError.toErrorMessage())
            is NetworkError.Unknown -> AppResult.Error(message = networkError.toErrorMessage(), throwable = networkError.throwable)
        }
    }
}
