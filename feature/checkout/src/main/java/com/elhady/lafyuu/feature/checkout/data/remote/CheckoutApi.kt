package com.elhady.lafyuu.feature.checkout.data.remote

import com.elhady.lafyuu.feature.checkout.data.remote.model.OrderCheckoutRequestDto
import com.elhady.lafyuu.feature.checkout.data.remote.model.OrderCheckoutResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface CheckoutApi {

    @POST("api/orders/checkout")
    suspend fun checkout(
        @Body request: OrderCheckoutRequestDto
    ): OrderCheckoutResponseDto
}
