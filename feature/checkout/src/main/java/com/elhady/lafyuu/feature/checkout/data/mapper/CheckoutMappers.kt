package com.elhady.lafyuu.feature.checkout.data.mapper

import com.elhady.lafyuu.feature.checkout.data.remote.model.AddressDto
import com.elhady.lafyuu.feature.checkout.data.remote.model.OrderCheckoutResponseDto
import com.elhady.lafyuu.feature.checkout.domain.model.Address
import com.elhady.lafyuu.feature.checkout.domain.model.CheckoutResult

fun AddressDto.toDomain(): Address {
    return Address(
        id = id ?: addressId.orEmpty(),
        state = state.orEmpty(),
        city = city.orEmpty(),
        street = street.orEmpty(),
        apartment = apartment.orEmpty(),
        phoneNumber = phoneNumber.orEmpty(),
        notes = notes.orEmpty()
    )
}

fun OrderCheckoutResponseDto.toDomain(): CheckoutResult {
    return CheckoutResult(
        message = message.orEmpty(),
        unifiedCheckoutUrl = unifiedCheckoutUrl,
        paymentClientSecret = paymentClientSecret
    )
}
