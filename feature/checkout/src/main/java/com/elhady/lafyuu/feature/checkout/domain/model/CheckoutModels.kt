package com.elhady.lafyuu.feature.checkout.domain.model

enum class PaymentMethod(val value: String) {
    CREDIT_CARD("Credit Card"),
    CASH_ON_DELIVERY("Cash on Delivery"),
    BANK_TRANSFER("Bank Transfer"),
    PAYPAL("Paypal")
}

data class ShippingAddress(
    val id: String,
    val state: String,
    val city: String,
    val street: String,
    val apartment: String,
    val phoneNumber: String
)

data class OrderSummary(
    val total: Double,
    val couponCode: String?
)

sealed interface CheckoutResult {
    data class Completed(val message: String) : CheckoutResult
    data class PaymentRequired(val paymentUrl: String, val message: String) : CheckoutResult
}