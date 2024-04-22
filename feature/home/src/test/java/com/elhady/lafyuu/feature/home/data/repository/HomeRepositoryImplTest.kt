package com.elhady.lafyuu.feature.home.data.repository

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.network.error.ApiErrorParser
import com.elhady.lafyuu.feature.home.data.remote.HomeApi
import com.elhady.lafyuu.feature.home.data.remote.model.CategoryDto
import com.elhady.lafyuu.feature.home.data.remote.model.GetAllCategoriesResponse
import com.elhady.lafyuu.feature.home.data.remote.model.GetAllOffersResponse
import com.elhady.lafyuu.feature.home.data.remote.model.OfferDto
import com.elhady.lafyuu.feature.home.data.remote.model.PagedListOfProductDto
import com.elhady.lafyuu.feature.home.data.remote.model.ProductDto
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class HomeRepositoryImplTest {

    private val homeApi: HomeApi = mockk()
    private val apiErrorParser: ApiErrorParser = mockk(relaxed = true)
    private lateinit var repository: HomeRepositoryImpl

    @Before
    fun setUp() {
        repository = HomeRepositoryImpl(homeApi, apiErrorParser)
    }

    @Test
    fun `getCategories returns Success on successful API response`() = runTest {
        val response = GetAllCategoriesResponse(
            categories = listOf(CategoryDto(id = "1", name = "Bags"))
        )
        coEvery { homeApi.getCategories() } returns Response.success(response)

        val result = repository.getCategories()

        assertTrue(result is AppResult.Success)
        val categories = (result as AppResult.Success).data
        assertEquals(1, categories.size)
        assertEquals("Bags", categories.first().name)
    }

    @Test
    fun `getOffers returns Success on successful API response`() = runTest {
        val response = GetAllOffersResponse(
            items = listOf(OfferDto(id = "10", name = "Flash Discount"))
        )
        coEvery { homeApi.getOffers() } returns Response.success(response)

        val result = repository.getOffers()

        assertTrue(result is AppResult.Success)
        val offers = (result as AppResult.Success).data
        assertEquals(1, offers.size)
        assertEquals("Flash Discount", offers.first().name)
    }

    @Test
    fun `getProducts returns Success with mapped products`() = runTest {
        val response = PagedListOfProductDto(
            items = listOf(ProductDto(id = "100", name = "Sneakers", price = 80.0))
        )
        coEvery { homeApi.getProducts(page = 1, pageSize = 10) } returns Response.success(response)

        val result = repository.getProducts(page = 1, pageSize = 10)

        assertTrue(result is AppResult.Success)
        val products = (result as AppResult.Success).data
        assertEquals(1, products.size)
        assertEquals("Sneakers", products.first().name)
    }
}
