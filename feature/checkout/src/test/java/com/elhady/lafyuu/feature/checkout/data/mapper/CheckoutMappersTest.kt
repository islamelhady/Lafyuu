package com.elhady.lafyuu.feature.checkout.data.mapper

import com.elhady.lafyuu.feature.checkout.data.remote.model.OrderCheckoutResponseDto
import com.elhady.lafyuu.feature.checkout.domain.model.CheckoutResult
import org.junit.Assert.assertEquals
import org.junit.Test

class CheckoutMappersTest {

    @Test
    fun `payment url means the user must pay`() {
        val result = OrderCheckoutResponseDto(message = "Pay now", unifiedCheckoutUrl = "https://pay.test/1").toDomain()

        assertEquals(CheckoutResult.PaymentRequired("https://pay.test/1", "Pay now"), result)
    }

    @Test
    fun `no url means the order is completed`() {
        assertEquals(CheckoutResult.Completed("Done"), OrderCheckoutResponseDto(message = "Done").toDomain())
    }

    @Test
    fun `blank url and client secret only are not a payment flow`() {
        val result = OrderCheckoutResponseDto(
            unifiedCheckoutUrl = " ",
            paymentClientSecret = "secret"
        ).toDomain()

        assertEquals(CheckoutResult.Completed(""), result)
    }
}