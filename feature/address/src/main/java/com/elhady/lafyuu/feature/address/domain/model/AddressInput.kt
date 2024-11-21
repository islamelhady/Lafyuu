package com.elhady.lafyuu.feature.address.domain.model

enum class AddressField(val apiName: String) {
    STATE("state"),
    CITY("city"),
    STREET("street"),
    APARTMENT("apartment"),
    PHONE("phoneNumber"),
    NOTES("notes")
}

enum class AddressFieldError { EMPTY, INVALID_PHONE }

data class AddressInput(
    val state: String,
    val city: String,
    val street: String,
    val apartment: String,
    val phoneNumber: String,
    val notes: String = ""
) {
    fun normalized() = copy(
        state = state.trim(),
        city = city.trim(),
        street = street.trim(),
        apartment = apartment.trim(),
        phoneNumber = phoneNumber.trim(),
        notes = notes.trim()
    )

    fun validate(): Map<AddressField, AddressFieldError> = buildMap {
        if (state.isBlank()) put(AddressField.STATE, AddressFieldError.EMPTY)
        if (city.isBlank()) put(AddressField.CITY, AddressFieldError.EMPTY)
        if (street.isBlank()) put(AddressField.STREET, AddressFieldError.EMPTY)
        if (apartment.isBlank()) put(AddressField.APARTMENT, AddressFieldError.EMPTY)
        when {
            phoneNumber.isBlank() -> put(AddressField.PHONE, AddressFieldError.EMPTY)
            phoneNumber.count { it.isDigit() } < MIN_PHONE_DIGITS ->
                put(AddressField.PHONE, AddressFieldError.INVALID_PHONE)
        }
    }

    private companion object {
        const val MIN_PHONE_DIGITS = 7
    }
}