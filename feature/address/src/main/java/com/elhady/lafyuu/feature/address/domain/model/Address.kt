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
    // Presentation text. Kept here only because feature:checkout still reads it,
    // move it to a UI mapper when checkout is refactored.
    // TODO: Refactor feature:checkout
    val fullAddress: String
    get() = listOf(street, apartment, city, state)
            .filter { it.isNotBlank() }
            .joinToString(", ")
}