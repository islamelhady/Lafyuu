package com.elhady.lafyuu.feature.cart.data.mapper

import com.elhady.lafyuu.feature.cart.data.remote.model.ApplyCouponResponseDto
import com.elhady.lafyuu.feature.cart.data.remote.model.CartItemResponseDto
import com.elhady.lafyuu.feature.cart.data.remote.model.CouponItemDto
import com.elhady.lafyuu.feature.cart.data.remote.model.GetCartResponseDto
import com.elhady.lafyuu.feature.cart.domain.model.Cart
import com.elhady.lafyuu.feature.cart.domain.model.CartItem
import com.elhady.lafyuu.feature.cart.domain.model.Coupon

fun GetCartResponseDto.toDomain(): Cart {
    return Cart(
        cartId = cartId.orEmpty(),
        items = cartItems?.map { it.toDomain() } ?: emptyList()
    )
}

fun CartItemResponseDto.toDomain(): CartItem {
    return CartItem(
        itemId = itemId.orEmpty(),
        productId = productId.orEmpty(),
        productName = productName.orEmpty(),
        productCoverUrl = productCoverUrl.orEmpty(),
        productStock = productStock ?: 0,
        weightInGrams = weightInGrams ?: 0.0,
        quantity = quantity ?: 1,
        discountPercentage = discountPercentage ?: 0.0,
        basePricePerUnit = basePricePerUnit ?: 0.0,
        finalPricePerUnit = finalPricePerUnit ?: 0.0,
        totalPrice = totalPrice ?: 0.0
    )
}

fun ApplyCouponResponseDto.toDomain(fallbackCartId: String = ""): Cart {
    return Cart(
        cartId = cartId ?: fallbackCartId,
        items = emptyList(),
        originalTotal = originalTotal,
        discountAmount = discountAmount,
        finalTotal = finalTotal,
        appliedCoupon = coupon?.toDomain()
    )
}

fun CouponItemDto.toDomain(): Coupon {
    return Coupon(
        code = code.orEmpty(),
        type = type.orEmpty(),
        value = value ?: 0.0
    )
}
