package com.elhady.lafyuu.feature.cart.data.remote

import com.elhady.lafyuu.feature.cart.data.remote.model.AddItemToCartRequestDto
import com.elhady.lafyuu.feature.cart.data.remote.model.AddItemToCartResponseDto
import com.elhady.lafyuu.feature.cart.data.remote.model.ApplyCouponRequestDto
import com.elhady.lafyuu.feature.cart.data.remote.model.ApplyCouponResponseDto
import com.elhady.lafyuu.feature.cart.data.remote.model.DecrementItemRequestDto
import com.elhady.lafyuu.feature.cart.data.remote.model.DecrementItemResponseDto
import com.elhady.lafyuu.feature.cart.data.remote.model.DeleteItemFromCartRequestDto
import com.elhady.lafyuu.feature.cart.data.remote.model.GetCartResponseDto
import com.elhady.lafyuu.feature.cart.data.remote.model.UpdateItemRequestDto
import com.elhady.lafyuu.feature.cart.data.remote.model.UpdateItemResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CartApi {

    @GET("api/cart")
    suspend fun getCart(): Response<GetCartResponseDto>

    @POST("api/cart/items")
    suspend fun addItemToCart(
        @Body request: AddItemToCartRequestDto
    ): Response<AddItemToCartResponseDto>

    @PUT("api/cart/items/{Id}")
    suspend fun updateCartItem(
        @Path("Id") id: String,
        @Body request: UpdateItemRequestDto
    ): Response<UpdateItemResponseDto>

    @POST("api/cart/items/decrement")
    suspend fun decrementCartItem(
        @Body request: DecrementItemRequestDto
    ): Response<DecrementItemResponseDto>

    @DELETE("api/cart/items/{Id}")
    suspend fun deleteCartItem(
        @Path("Id") id: String,
        @Body request: DeleteItemFromCartRequestDto
    ): Response<Any>

    @POST("api/cart/apply-coupon")
    suspend fun applyCoupon(
        @Body request: ApplyCouponRequestDto
    ): Response<ApplyCouponResponseDto>
}
