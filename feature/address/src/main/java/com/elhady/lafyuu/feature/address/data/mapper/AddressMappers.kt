package com.elhady.lafyuu.feature.address.data.mapper

import com.elhady.lafyuu.feature.address.data.remote.model.AddressDto
import com.elhady.lafyuu.feature.address.domain.model.Address

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
