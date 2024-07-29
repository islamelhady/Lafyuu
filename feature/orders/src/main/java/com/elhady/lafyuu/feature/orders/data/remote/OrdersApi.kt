package com.elhady.lafyuu.feature.orders.data.remote

import com.elhady.lafyuu.feature.orders.data.remote.model.GetAllOrdersResponseDto
import com.elhady.lafyuu.feature.orders.data.remote.model.GetOrdersHistoryResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface OrdersApi {
    @GET("api/orders")
    suspend fun getOrders(): Response<GetAllOrdersResponseDto>

    @GET("api/orders/history/{orderId}")
    suspend fun getOrderHistory(
        @Path("orderId") orderId: String
    ): Response<GetOrdersHistoryResponseDto>
}
