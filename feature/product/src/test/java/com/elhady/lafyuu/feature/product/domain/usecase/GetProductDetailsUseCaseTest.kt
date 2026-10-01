package com.elhady.lafyuu.feature.product.domain.usecase

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.product.domain.model.Product
import com.elhady.lafyuu.feature.product.domain.model.ProductDetails
import com.elhady.lafyuu.feature.product.domain.model.ProductReview
import com.elhady.lafyuu.feature.product.domain.repository.ProductRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetProductDetailsUseCaseTest {

    private val repository: ProductRepository = mockk()
    private lateinit var useCase: GetProductDetailsUseCase

    @Before
    fun setUp() {
        useCase = GetProductDetailsUseCase(repository)
    }

    @Test
    fun `returns error when product ID is blank`() = runTest {
        val result = useCase("")

        assertTrue(result is AppResult.Error)
        assertEquals("Invalid product ID", (result as AppResult.Error).message)
    }

    @Test
    fun `returns success content when details request succeeds`() = runTest {
        val details = ProductDetails(id = "p-1", name = "Main Product")
        val review = ProductReview(comment = "Good")
        val recommended = Product(id = "p-2", name = "Rec Product")

        coEvery { repository.getProductDetails("p-1") } returns AppResult.Success(details)
        coEvery { repository.getProductReviews("p-1", 1, 5) } returns AppResult.Success(listOf(review))
        coEvery { repository.getRecommendedProducts(10) } returns AppResult.Success(listOf(recommended))

        val result = useCase("p-1")

        assertTrue(result is AppResult.Success)
        val content = (result as AppResult.Success).data
        assertEquals("p-1", content.details.id)
        assertEquals(1, content.reviews.size)
        assertEquals(1, content.recommendedProducts.size)
    }

    @Test
    fun `returns error when product details request fails`() = runTest {
        coEvery { repository.getProductDetails("p-1") } returns AppResult.Error("Product not found")
        coEvery { repository.getProductReviews("p-1", 1, 5) } returns AppResult.Success(emptyList())
        coEvery { repository.getRecommendedProducts(10) } returns AppResult.Success(emptyList())

        val result = useCase("p-1")

        assertTrue(result is AppResult.Error)
        assertEquals("Product not found", (result as AppResult.Error).message)
    }
}
