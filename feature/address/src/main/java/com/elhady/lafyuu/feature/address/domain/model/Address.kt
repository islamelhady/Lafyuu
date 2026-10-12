package com.elhady.lafyuu.feature.address.domain.model

data class Address(
    val id: String,
    val state: String,
    val city: String,
    val street: String,
    val apartment: String,
    val phoneNumber: String,
    val notes: String
) {
    val fullAddress: String
        get() = "$street, $apartment, $city, $state"
}
