package com.elhady.lafyuu.feature.product.data.repository

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.network.error.ApiErrorParser
import com.elhady.lafyuu.feature.product.data.remote.ProductApi
import com.elhady.lafyuu.feature.product.data.remote.model.AddItemToCartResponseDto
import com.elhady.lafyuu.feature.product.data.remote.model.GetProductReviewsResponseDto
import com.elhady.lafyuu.feature.product.data.remote.model.PagedListOfProductDto
import com.elhady.lafyuu.feature.product.data.remote.model.PagedListOfUserReviewDto
import com.elhady.lafyuu.feature.product.data.remote.model.ProductDto
import com.elhady.lafyuu.feature.product.data.remote.model.UserReviewDto
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class ProductRepositoryImplTest {

    private val productApi: ProductApi = mockk()
    private val apiErrorParser: ApiErrorParser = mockk(relaxed = true)

    private lateinit var repository: ProductRepositoryImpl

    @Before
    fun setUp() {
        repository = ProductRepositoryImpl(productApi, apiErrorParser)
    }

    @Test
    fun `getProductDetails returns success when response is successful`() = runTest {
        val dto = ProductDto(id = "123", name = "Test Product", price = 99.0)
        coEvery { productApi.getProductDetails("123") } returns Response.success(dto)

        val result = repository.getProductDetails("123")

        assertTrue(result is AppResult.Success)
        val data = (result as AppResult.Success).data
        assertEquals("123", data.id)
        assertEquals("Test Product", data.name)
    }

    @Test
    fun `getProductReviews returns list of reviews on success`() = runTest {
        val reviewDto = UserReviewDto(comment = "Awesome", rating = 5, userName = "Alice")
        val responseDto = GetProductReviewsResponseDto(
            reviews = PagedListOfUserReviewDto(items = listOf(reviewDto))
        )
        coEvery { productApi.getProductReviews("123", 1, 10) } returns Response.success(responseDto)

        val result = repository.getProductReviews("123", 1, 10)

        assertTrue(result is AppResult.Success)
        val list = (result as AppResult.Success).data
        assertEquals(1, list.size)
        assertEquals("Awesome", list.first().comment)
    }

    @Test
    fun `getRecommendedProducts returns list of products on success`() = runTest {
        val pagedDto = PagedListOfProductDto(
            items = listOf(ProductDto(id = "rec-1", name = "Recommended Shoe"))
        )
        coEvery { productApi.getProducts(pageSize = 10) } returns Response.success(pagedDto)

        val result = repository.getRecommendedProducts(10)

        assertTrue(result is AppResult.Success)
        val list = (result as AppResult.Success).data
        assertEquals(1, list.size)
        assertEquals("rec-1", list.first().id)
    }

    @Test
    fun `addToCart returns success when API responds successfully`() = runTest {
        coEvery { productApi.addItemToCart(any()) } returns Response.success(AddItemToCartResponseDto("Added"))

        val result = repository.addToCart("123", 2)

        assertTrue(result is AppResult.Success)
    }
}
