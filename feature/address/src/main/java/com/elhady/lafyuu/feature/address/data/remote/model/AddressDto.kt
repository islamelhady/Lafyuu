package com.elhady.lafyuu.feature.address.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class AddressDto(
    val id: String? = null,
    val addressId: String? = null,
    val state: String? = null,
    val city: String? = null,
    val street: String? = null,
    val apartment: String? = null,
    val phoneNumber: String? = null,
    val notes: String? = null
)

@Serializable
data class CreateAddressRequestDto(
    val state: String,
    val city: String,
    val street: String,
    val apartment: String,
    val phoneNumber: String,
    val notes: String
)

@Serializable
data class UpdateAddressRequestDto(
    val id: String,
    val state: String,
    val city: String,
    val street: String,
    val apartment: String,
    val phoneNumber: String,
    val notes: String
)
