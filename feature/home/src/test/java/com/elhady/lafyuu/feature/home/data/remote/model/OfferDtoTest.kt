package com.elhady.lafyuu.feature.home.data.remote.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class OfferDtoTest {

    @Test
    fun `allOffers returns items when items is not empty`() {
        val offer1 = OfferDto(id = "1", name = "Offer 1")
        val offer2 = OfferDto(id = "2", name = "Offer 2")
        val response = GetAllOffersResponse(
            items = listOf(offer1),
            offers = listOf(offer2),
            data = emptyList()
        )

        assertEquals(listOf(offer1), response.allOffers)
    }

    @Test
    fun `allOffers falls back to offers when items is empty`() {
        val offer2 = OfferDto(id = "2", name = "Offer 2")
        val offer3 = OfferDto(id = "3", name = "Offer 3")
        val response = GetAllOffersResponse(
            items = emptyList(),
            offers = listOf(offer2),
            data = listOf(offer3)
        )

        assertEquals(listOf(offer2), response.allOffers)
    }

    @Test
    fun `allOffers falls back to data when items and offers are empty`() {
        val offer3 = OfferDto(id = "3", name = "Offer 3")
        val response = GetAllOffersResponse(
            items = emptyList(),
            offers = emptyList(),
            data = listOf(offer3)
        )

        assertEquals(listOf(offer3), response.allOffers)
    }

    @Test
    fun `allOffers returns empty list when items offers and data are null or empty`() {
        val response = GetAllOffersResponse(
            items = null,
            offers = null,
            data = null
        )

        assertTrue(response.allOffers.isEmpty())
    }

    @Test
    fun `OfferDto default values are null`() {
        val dto = OfferDto()

        assertNull(dto.id)
        assertNull(dto.name)
        assertNull(dto.title)
        assertNull(dto.description)
        assertNull(dto.coverUrl)
        assertNull(dto.coverPictureUrl)
        assertNull(dto.imageUrl)
        assertNull(dto.createdAt)
    }

    @Test
    fun `deserializes wrapped offers json successfully`() {
        val jsonString = "{\"offers\":{\"items\":[{\"id\":\"917299cd-3e8b\",\"name\":\"Offer 1\"}]}}"
        val response = kotlinx.serialization.json.Json.decodeFromString<GetAllOffersResponse>(jsonString)
        assertEquals(1, response.allOffers.size)
        assertEquals("Offer 1", response.allOffers.first().name)
    }
}
