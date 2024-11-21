package com.elhady.lafyuu.feature.address.data.mapper

import com.elhady.lafyuu.feature.address.data.remote.model.AddressDto
import com.elhady.lafyuu.feature.address.data.remote.model.CreateAddressRequestDto
import com.elhady.lafyuu.feature.address.data.remote.model.UpdateAddressRequestDto
import com.elhady.lafyuu.feature.address.domain.model.Address
import com.elhady.lafyuu.feature.address.domain.model.AddressInput

fun AddressDto.toDomain() = Address(
    id = id.orEmpty(),
    state = state.orEmpty(),
    city = city.orEmpty(),
    street = street.orEmpty(),
    apartment = apartment.orEmpty(),
    phoneNumber = phoneNumber.orEmpty(),
    notes = notes.orEmpty()
)

fun AddressInput.toCreateRequest() = CreateAddressRequestDto(
    state = state,
    city = city,
    street = street,
    apartment = apartment,
    phoneNumber = phoneNumber,
    notes = notes
)

fun AddressInput.toUpdateRequest(addressId: String) = UpdateAddressRequestDto(
    id = addressId,
    state = state,
    city = city,
    street = street,
    apartment = apartment,
    phoneNumber = phoneNumber,
    notes = notes
)