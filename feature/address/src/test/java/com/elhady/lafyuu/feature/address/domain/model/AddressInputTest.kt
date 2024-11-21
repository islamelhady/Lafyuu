package com.elhady.lafyuu.feature.address.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AddressInputTest {

    private val valid = AddressInput(
        state = "Cairo", city = "Nasr City", street = "Street 9",
        apartment = "12", phoneNumber = "01012345678"
    )

    @Test
    fun `valid input has no errors`() {
        assertTrue(valid.validate().isEmpty())
    }

    @Test
    fun `blank required fields are reported and notes stay optional`() {
        val errors = AddressInput("", " ", "", "  ", "").validate()

        assertEquals(
            setOf(AddressField.STATE, AddressField.CITY, AddressField.STREET, AddressField.APARTMENT, AddressField.PHONE),
            errors.keys
        )
        assertTrue(errors.values.all { it == AddressFieldError.EMPTY })
    }

    @Test
    fun `phone with too few digits is invalid`() {
        val errors = valid.copy(phoneNumber = "12-34").validate()

        assertEquals(AddressFieldError.INVALID_PHONE, errors[AddressField.PHONE])
    }

    @Test
    fun `phone formatting characters are allowed`() {
        assertTrue(valid.copy(phoneNumber = "+20 101 234 5678").validate().isEmpty())
    }

    @Test
    fun `normalized trims every field`() {
        val clean = AddressInput(" Cairo ", " Nasr ", " St ", " 1 ", " 0101234567 ", " note ").normalized()

        assertEquals(AddressInput("Cairo", "Nasr", "St", "1", "0101234567", "note"), clean)
    }

    @Test
    fun `fullAddress skips blank parts`() {
        val address = Address("1", "Cairo", "Nasr City", "Street 9", "", "0101", "")

        assertEquals("Street 9, Nasr City, Cairo", address.fullAddress)
    }
}