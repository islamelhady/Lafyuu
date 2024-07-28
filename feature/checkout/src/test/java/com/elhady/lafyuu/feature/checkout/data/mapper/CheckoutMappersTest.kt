package com.elhady.lafyuu.feature.checkout.data.mapper

import com.elhady.lafyuu.feature.checkout.data.remote.model.AddressDto
import com.elhady.lafyuu.feature.checkout.data.remote.model.OrderCheckoutResponseDto
import org.junit.Assert.assertEquals
import org.junit.Test

class CheckoutMappersTest {

    @Test
    fun `maps AddressDto to domain Address correctly`() {
        val dto = AddressDto(id = "a-1", city = "Cairo", state = "Cairo", street = "Nile St", apartment = "10", phoneNumber = "12345", notes = "")
        val address = dto.toDomain()
        assertEquals("a-1", address.id)
        assertEquals("Cairo", address.city)
        assertEquals("Nile St", address.street)
    }

    @Test
    fun `maps OrderCheckoutResponseDto to domain CheckoutResult correctly`() {
        val dto = OrderCheckoutResponseDto(message = "Success", unifiedCheckoutUrl = "https://checkout.url", paymentClientSecret = "secret")
        val result = dto.toDomain()
        assertEquals("Success", result.message)
        assertEquals("https://checkout.url", result.unifiedCheckoutUrl)
        assertEquals("secret", result.paymentClientSecret)
    }
}
