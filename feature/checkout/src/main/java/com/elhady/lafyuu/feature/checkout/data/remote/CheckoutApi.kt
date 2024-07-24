package com.elhady.lafyuu.feature.checkout.data.remote

import com.elhady.lafyuu.feature.checkout.data.remote.model.AddressDto
import com.elhady.lafyuu.feature.checkout.data.remote.model.CreateAddressRequestDto
import com.elhady.lafyuu.feature.checkout.data.remote.model.OrderCheckoutRequestDto
import com.elhady.lafyuu.feature.checkout.data.remote.model.OrderCheckoutResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CheckoutApi {

    @GET("api/addresses")
    suspend fun getAddresses(): Response<List<AddressDto>>

    @POST("api/addresses")
    suspend fun createAddress(
        @Body request: CreateAddressRequestDto
    ): Response<AddressDto>

    @DELETE("api/addresses/{Id}")
    suspend fun deleteAddress(
        @Path("Id") id: String
    ): Response<Any>

    @POST("api/orders/checkout")
    suspend fun checkout(
        @Body request: OrderCheckoutRequestDto
    ): Response<OrderCheckoutResponseDto>
}
