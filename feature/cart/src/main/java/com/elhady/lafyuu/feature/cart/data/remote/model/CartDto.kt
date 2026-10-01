package com.elhady.lafyuu.feature.cart.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class GetCartResponseDto(
    val cartId: String? = null,
    val cartItems: List<CartItemResponseDto>? = null
)

@Serializable
data class CartItemResponseDto(
    val itemId: String? = null,
    val productId: String? = null,
    val productName: String? = null,
    val productCoverUrl: String? = null,
    val productStock: Int? = null,
    val weightInGrams: Double? = null,
    val quantity: Int? = null,
    val discountPercentage: Double? = null,
    val basePricePerUnit: Double? = null,
    val finalPricePerUnit: Double? = null,
    val totalPrice: Double? = null
)

@Serializable
data class AddItemToCartRequestDto(
    val productId: String,
    val quantity: Int
)

@Serializable
data class AddItemToCartResponseDto(
    val message: String? = null,
    val id: String? = null,
    val productId: String? = null,
    val quantity: Int? = null
)

@Serializable
data class UpdateItemRequestDto(
    val quantity: Int
)

@Serializable
data class UpdateItemResponseDto(
    val message: String? = null,
    val id: String? = null,
    val productId: String? = null,
    val quantity: Int? = null
)

@Serializable
data class DecrementItemRequestDto(
    val itemId: String,
    val quantity: Int
)

@Serializable
data class DecrementItemResponseDto(
    val message: String? = null,
    val itemId: String? = null,
    val productId: String? = null,
    val quantity: Int? = null
)

@Serializable
data class DeleteItemFromCartRequestDto(
    val id: String
)

@Serializable
data class ApplyCouponRequestDto(
    val couponCode: String
)

@Serializable
data class ApplyCouponResponseDto(
    val cartId: String? = null,
    val originalTotal: Double? = null,
    val discountAmount: Double? = null,
    val finalTotal: Double? = null,
    val coupon: CouponItemDto? = null
)

@Serializable
data class CouponItemDto(
    val code: String? = null,
    val type: String? = null,
    val value: Double? = null
)
