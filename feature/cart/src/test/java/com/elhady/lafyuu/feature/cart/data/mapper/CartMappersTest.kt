package com.elhady.lafyuu.feature.cart.data.mapper

import com.elhady.lafyuu.feature.cart.data.remote.model.ApplyCouponResponseDto
import com.elhady.lafyuu.feature.cart.data.remote.model.CartItemResponseDto
import com.elhady.lafyuu.feature.cart.data.remote.model.CouponItemDto
import com.elhady.lafyuu.feature.cart.data.remote.model.GetCartResponseDto
import org.junit.Assert.assertEquals
import org.junit.Test

class CartMappersTest {

    @Test
    fun `maps GetCartResponseDto to domain Cart correctly`() {
        val dto = GetCartResponseDto(
            cartId = "cart-1",
            cartItems = listOf(
                CartItemResponseDto(
                    itemId = "item-1",
                    productId = "prod-1",
                    productName = "Shoes",
                    quantity = 2,
                    totalPrice = 100.0
                )
            )
        )

        val cart = dto.toDomain()

        assertEquals("cart-1", cart.cartId)
        assertEquals(1, cart.items.size)
        assertEquals("item-1", cart.items[0].itemId)
        assertEquals("Shoes", cart.items[0].productName)
        assertEquals(2, cart.items[0].quantity)
        assertEquals(100.0, cart.items[0].totalPrice, 0.001)
    }

    @Test
    fun `maps ApplyCouponResponseDto to domain Cart summary correctly`() {
        val dto = ApplyCouponResponseDto(
            cartId = "cart-1",
            originalTotal = 200.0,
            discountAmount = 20.0,
            finalTotal = 180.0,
            coupon = CouponItemDto(code = "SALE10", type = "PERCENTAGE", value = 10.0)
        )

        val cart = dto.toDomain()

        assertEquals("cart-1", cart.cartId)
        assertEquals(200.0, cart.originalTotal!!, 0.001)
        assertEquals(20.0, cart.discountAmount!!, 0.001)
        assertEquals(180.0, cart.finalTotal!!, 0.001)
        assertEquals("SALE10", cart.appliedCoupon?.code)
    }
}
