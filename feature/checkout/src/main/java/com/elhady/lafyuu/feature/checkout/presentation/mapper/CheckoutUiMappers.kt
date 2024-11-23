package com.elhady.lafyuu.feature.checkout.presentation.mapper

import com.elhady.lafyuu.feature.checkout.domain.model.ShippingAddress
import com.elhady.lafyuu.feature.checkout.presentation.model.ShippingAddressUi

fun ShippingAddress.toUi() = ShippingAddressUi(
    id = id,
    title = listOf(city, state).filter { it.isNotBlank() }.joinToString(", "),
    fullAddress = listOf(street, apartment, city, state)
        .filter { it.isNotBlank() }
        .joinToString(", "),
    phone = if (phoneNumber.startsWith("+")) phoneNumber else "+2 $phoneNumber"
)