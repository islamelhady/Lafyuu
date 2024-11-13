package com.elhady.lafyuu.feature.cart.presentation.model

data class CartUi(
    val items: List<CartItemUi>,
    val summary: CartSummaryUi
)

data class CartItemUi(
    val itemId: String,
    val productId: String,
    val name: String,
    val imageUrl: String,
    val price: String,
    val quantity: Int
)

data class CartSummaryUi(
    val itemsCount: Int,
    val itemsTotal: String,
    val shipping: String,
    val importCharges: String,
    val discount: String,
    val total: String,
    val appliedCouponCode: String?
)