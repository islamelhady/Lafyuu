package com.elhady.lafyuu.feature.home.data.mapper

import com.elhady.lafyuu.feature.home.data.remote.model.CategoryDto
import com.elhady.lafyuu.feature.home.data.remote.model.OfferDto
import com.elhady.lafyuu.feature.home.data.remote.model.ProductDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class HomeMappersTest {

    @Test
    fun `price is base price and final price applies the discount`() {
        val domain = ProductDto(id = "10", name = "Ring", price = 100.0, discountPercentage = 20.0, rating = 4.5).toDomain()

        assertEquals(100.0, domain.basePrice, 0.001)
        assertEquals(20, domain.discountPercentage)
        assertEquals(80.0, domain.finalPrice, 0.001)
        assertEquals(4.5f, domain.rating)
    }

    @Test
    fun `product without discount keeps its price`() {
        val domain = ProductDto(id = "11", name = "Bag", price = 150.0, discountPercentage = 0.0).toDomain()

        assertFalse(domain.hasDiscount)
        assertEquals(150.0, domain.finalPrice, 0.001)
    }

    @Test
    fun `discount is clamped between 0 and 100`() {
        assertEquals(100, ProductDto(price = 10.0, discountPercentage = 150.0).toDomain().discountPercentage)
        assertEquals(0, ProductDto(price = 10.0, discountPercentage = -5.0).toDomain().discountPercentage)
    }

    @Test
    fun `CategoryDto maps all fields`() {
        val domain = CategoryDto(id = "1", name = "Rings", description = "Jewelry", coverPictureUrl = "u").toDomain()

        assertEquals("1", domain.id)
        assertEquals("Rings", domain.name)
        assertEquals("u", domain.coverPictureUrl)
    }

    @Test
    fun `OfferDto maps all fields`() {
        val domain = OfferDto(id = "100", name = "Summer", description = "Big", coverUrl = "u").toDomain()

        assertEquals("Summer", domain.name)
        assertEquals("u", domain.coverUrl)
    }
}