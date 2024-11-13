package com.elhady.lafyuu.feature.cart.presentation.mapper

import com.elhady.lafyuu.feature.cart.domain.model.Cart
import com.elhady.lafyuu.feature.cart.domain.model.CartItem
import com.elhady.lafyuu.feature.cart.presentation.model.CartItemUi
import com.elhady.lafyuu.feature.cart.presentation.model.CartSummaryUi
import com.elhady.lafyuu.feature.cart.presentation.model.CartUi
import java.util.Locale

fun Cart.toUi(): CartUi {
    val discount = discountAmount ?: 0.0
    return CartUi(
        items = items.map { it.toUi() },
        summary = CartSummaryUi(
            itemsCount = items.sumOf { it.quantity },
            itemsTotal = formatPrice(originalTotal ?: itemsTotal),
            // Assumption: the API doesn't expose shipping/import charges in the cart
            shipping = formatPrice(0.0),
            importCharges = formatPrice(0.0),
            discount = if (discount > 0) "-${formatPrice(discount)}" else formatPrice(0.0),
            total = formatPrice(finalTotal ?: itemsTotal),
            appliedCouponCode = appliedCoupon?.code
        )
    )
}

private fun CartItem.toUi() = CartItemUi(
    itemId = itemId,
    productId = productId,
    name = productName,
    imageUrl = productCoverUrl,
    price = formatPrice(finalPricePerUnit),
    quantity = quantity
)

private fun formatPrice(value: Double): String = String.format(Locale.US, "\$%.2f", value)