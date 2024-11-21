package com.elhady.lafyuu.feature.address.presentation.mapper

import com.elhady.lafyuu.feature.address.domain.model.Address
import com.elhady.lafyuu.feature.address.presentation.model.AddressUi

fun Address.toUi() = AddressUi(
    id = id,
    title = listOf(city, state).filter { it.isNotBlank() }.joinToString(", "),
    fullAddress = fullAddress,
    phone = formatPhone(phoneNumber)
)

// Assumption: the app targets Egypt (+2) and the API stores no country code
private fun formatPhone(raw: String): String = if (raw.startsWith("+")) raw else "+2 $raw"