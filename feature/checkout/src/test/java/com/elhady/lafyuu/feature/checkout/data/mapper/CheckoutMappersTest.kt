package com.elhady.lafyuu.feature.checkout.data.mapper

import com.elhady.lafyuu.feature.checkout.data.remote.model.OrderCheckoutResponseDto
import org.junit.Assert.assertEquals
import org.junit.Test

class CheckoutMappersTest {

    @Test
    fun `maps OrderCheckoutResponseDto to domain CheckoutResult correctly`() {
        val dto = OrderCheckoutResponseDto(message = "Success", unifiedCheckoutUrl = "https://checkout.url", paymentClientSecret = "secret")
        val result = dto.toDomain()
        assertEquals("Success", result.message)
        assertEquals("https://checkout.url", result.unifiedCheckoutUrl)
        assertEquals("secret", result.paymentClientSecret)
    }
}
