package com.elhady.lafyuu.feature.cart.domain.model

data class Cart(
    val cartId: String,
    val items: List<CartItem>,
    val originalTotal: Double? = null,
    val discountAmount: Double? = null,
    val finalTotal: Double? = null,
    val appliedCoupon: Coupon? = null
) {
    val itemsTotal: Double
        get() = items.sumOf { it.totalPrice }
}

data class CartItem(
    val itemId: String,
    val productId: String,
    val productName: String,
    val productCoverUrl: String,
    val productStock: Int,
    val weightInGrams: Double,
    val quantity: Int,
    val discountPercentage: Double,
    val basePricePerUnit: Double,
    val finalPricePerUnit: Double,
    val totalPrice: Double
)

data class Coupon(
    val code: String,
    val type: String,
    val value: Double
)
