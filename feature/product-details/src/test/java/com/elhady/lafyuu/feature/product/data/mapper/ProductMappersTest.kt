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
            discountPercentage = 33.0,
            stock = 10,
            color = "Red",
            rating = 4.5,
            reviewsCount = 12,
            sellerId = "seller-1"
        )

        val domain = dto.toProductDetails()

        assertEquals("prod-1", domain.id)
        assertEquals("CODE123", domain.productCode)
        assertEquals("Test Shoes", domain.name)
        assertEquals("A great pair of shoes", domain.description)
        assertEquals("https://example.com/cover.jpg", domain.pictureUrls[0])
        assertEquals("https://example.com/pic1.jpg", domain.pictureUrls[1])
        assertEquals("https://example.com/pic2.jpg", domain.pictureUrls[2])
        assertEquals(3, domain.pictureUrls.size)
        assertEquals(100.0, domain.basePrice, 0.01)
        assertEquals(67.0, domain.finalPrice, 0.01)
        assertEquals(33, domain.discountPercentage)
        assertEquals(10, domain.stock)
        assertEquals("Red", domain.color)
        assertEquals(5, domain.rating)
        assertEquals(12, domain.reviewsCount)
        assertEquals("seller-1", domain.sellerId)
    }

    @Test
    fun `toProductSummaryDomain maps product summary correctly`() {
        val dto = ProductDto(
            id = "prod-2",
            name = "Summary Shoe",
            coverPictureUrl = "https://example.com/summary.jpg",
            price = 50.0,
            discountPercentage = 20.0,
            rating = 4.0
        )

        val domain = dto.toRecommendedProduct()

        assertEquals("prod-2", domain.id)
        assertEquals("Summary Shoe", domain.name)
        assertEquals("https://example.com/summary.jpg", domain.imageUrl)
        assertEquals(50.0, domain.basePrice, 0.01)
        assertEquals(20, domain.discountPercentage)
        assertEquals(4, domain.rating)
        assertNotNull(domain.finalPrice)
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

        val domain = dto.toReview()

        assertEquals("Great product!", domain.comment)
        assertEquals(5, domain.rating)
        assertEquals("2024-04-28", domain.createdAt)
        assertEquals("John Doe", domain.userName)
        assertEquals("https://example.com/avatar.jpg", domain.userPicture)
    }
}
