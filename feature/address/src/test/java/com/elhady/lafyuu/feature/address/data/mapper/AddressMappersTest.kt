package com.elhady.lafyuu.feature.address.data.mapper

import com.elhady.lafyuu.feature.address.data.remote.model.AddressDto
import com.elhady.lafyuu.feature.address.domain.model.AddressInput
import org.junit.Assert.assertEquals
import org.junit.Test

class AddressMappersTest {

    @Test
    fun `missing fields get safe defaults`() {
        val address = AddressDto(id = "a-1").toDomain()

        assertEquals("a-1", address.id)
        assertEquals("", address.city)
        assertEquals("", address.notes)
    }

    @Test
    fun `update request carries the address id`() {
        val request = AddressInput("Cairo", "Nasr City", "St", "1", "0101234567", "n").toUpdateRequest("a-1")

        assertEquals("a-1", request.id)
        assertEquals("Cairo", request.state)
        assertEquals("0101234567", request.phoneNumber)
    }

    @Test
    fun `create request maps every field`() {
        val request = AddressInput("Cairo", "Nasr City", "St", "1", "0101234567", "n").toCreateRequest()

        assertEquals("Nasr City", request.city)
        assertEquals("n", request.notes)
    }
}