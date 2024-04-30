package com.elhady.lafyuu.feature.product.data.mapper

import com.elhady.lafyuu.feature.product.data.remote.model.ProductDto
import com.elhady.lafyuu.feature.product.data.remote.model.UserReviewDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class ProductMappersTest {

    @Test
    fun `toProductDetailsDomain maps product dto correctly`() {
        val dto = ProductDto(
            id = "prod-1",
            productCode = "CODE123",
            name = "Test Shoes",
            description = "A great pair of shoes",
            coverPictureUrl = "https://example.com/cover.jpg",
            productPictures = listOf("https://example.com/pic1.jpg", "https://example.com/pic2.jpg"),
            price = 100.0,
            originalPrice = 150.0,
            discountPercentage = 33.0,
            stock = 10,
            color = "Red",
            rating = 4.5,
            reviewsCount = 12,
            sellerId = "seller-1"
        )

        val domain = dto.toProductDetailsDomain()

        assertEquals("prod-1", domain.id)
        assertEquals("CODE123", domain.productCode)
        assertEquals("Test Shoes", domain.name)
        assertEquals("A great pair of shoes", domain.description)
        assertEquals("https://example.com/cover.jpg", domain.coverPictureUrl)
        assertEquals(2, domain.productPictures.size)
        assertEquals(100.0, domain.price, 0.01)
        assertEquals(150.0, domain.originalPrice!!, 0.01)
        assertEquals(33, domain.discountPercentage)
        assertEquals(10, domain.stock)
        assertEquals("Red", domain.color)
        assertEquals(4.5f, domain.rating)
        assertEquals(12, domain.reviewsCount)
        assertEquals("seller-1", domain.sellerId)
    }

    @Test
    fun `toProductSummaryDomain maps product summary correctly`() {
        val dto = ProductDto(
            id = "prod-2",
            title = "Summary Shoe",
            coverUrl = "https://example.com/summary.jpg",
            price = 50.0,
            discountPercentage = 20.0,
            rating = 4.0
        )

        val domain = dto.toProductSummaryDomain()

        assertEquals("prod-2", domain.id)
        assertEquals("Summary Shoe", domain.name)
        assertEquals("https://example.com/summary.jpg", domain.imageUrl)
        assertEquals(50.0, domain.price, 0.01)
        assertEquals(20, domain.discountPercentage)
        assertEquals(4.0f, domain.rating)
        assertNotNull(domain.originalPrice)
    }

    @Test
    fun `UserReviewDto toDomain maps review correctly`() {
        val dto = UserReviewDto(
            comment = "Great product!",
            rating = 5,
            createdAt = "2024-04-28",
            userName = "John Doe",
            userPicture = "https://example.com/avatar.jpg"
        )

        val domain = dto.toDomain()

        assertEquals("Great product!", domain.comment)
        assertEquals(5f, domain.rating)
        assertEquals("2024-04-28", domain.createdAt)
        assertEquals("John Doe", domain.userName)
        assertEquals("https://example.com/avatar.jpg", domain.userPicture)
    }
}
