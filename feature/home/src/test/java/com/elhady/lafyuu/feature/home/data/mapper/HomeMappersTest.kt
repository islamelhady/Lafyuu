package com.elhady.lafyuu.feature.home.data.mapper

import com.elhady.lafyuu.feature.home.data.remote.model.CategoryDto
import com.elhady.lafyuu.feature.home.data.remote.model.OfferDto
import com.elhady.lafyuu.feature.home.data.remote.model.ProductDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class HomeMappersTest {

    @Test
    fun `CategoryDto toDomain maps all fields correctly`() {
        val dto = CategoryDto(
            id = "1",
            name = "Shoes",
            description = "Footwear",
            coverPictureUrl = "https://example.com/shoes.png"
        )

        val domain = dto.toDomain()

        assertEquals("1", domain.id)
        assertEquals("Shoes", domain.name)
        assertEquals("Footwear", domain.description)
        assertEquals("https://example.com/shoes.png", domain.coverPictureUrl)
    }

    @Test
    fun `ProductDto toDomain calculates discounted price correctly when discount percentage exists`() {
        val dto = ProductDto(
            id = "10",
            name = "Nike Air Zoom",
            price = 100.0,
            discountPercentage = 20.0,
            coverPictureUrl = "https://example.com/nike.png",
            rating = 4.5
        )

        val domain = dto.toDomain()

        assertEquals("10", domain.id)
        assertEquals("Nike Air Zoom", domain.name)
        assertEquals(80.0, domain.price, 0.001)
        assertEquals(100.0, domain.originalPrice!!, 0.001)
        assertEquals("20% OFF", domain.discountLabel)
        assertEquals(4.5f, domain.rating)
    }

    @Test
    fun `ProductDto toDomain without discount sets originalPrice to null`() {
        val dto = ProductDto(
            id = "11",
            name = "Puma Running",
            price = 150.0,
            discountPercentage = 0.0
        )

        val domain = dto.toDomain()

        assertEquals(150.0, domain.price, 0.001)
        assertNull(domain.originalPrice)
        assertNull(domain.discountLabel)
    }

    @Test
    fun `OfferDto toDomain maps fields correctly`() {
        val dto = OfferDto(
            id = "100",
            name = "Summer Sale",
            description = "Big discounts",
            coverUrl = "https://example.com/banner.png"
        )

        val domain = dto.toDomain()

        assertEquals("100", domain.id)
        assertEquals("Summer Sale", domain.name)
        assertEquals("Big discounts", domain.description)
        assertEquals("https://example.com/banner.png", domain.coverUrl)
    }
}
