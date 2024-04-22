package com.elhady.lafyuu.feature.home.domain.usecase

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.home.domain.model.Category
import com.elhady.lafyuu.feature.home.domain.model.Offer
import com.elhady.lafyuu.feature.home.domain.model.Product
import com.elhady.lafyuu.feature.home.domain.repository.HomeRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetHomeUseCasesTest {

    private val homeRepository: HomeRepository = mockk()
    private lateinit var getCategoriesUseCase: GetCategoriesUseCase
    private lateinit var getOffersUseCase: GetOffersUseCase
    private lateinit var getProductsUseCase: GetProductsUseCase

    @Before
    fun setUp() {
        getCategoriesUseCase = GetCategoriesUseCase(homeRepository)
        getOffersUseCase = GetOffersUseCase(homeRepository)
        getProductsUseCase = GetProductsUseCase(homeRepository)
    }

    @Test
    fun `GetCategoriesUseCase delegates to repository`() = runTest {
        val expected = listOf(Category("1", "Shirts", null, null))
        coEvery { homeRepository.getCategories() } returns AppResult.Success(expected)

        val result = getCategoriesUseCase()

        assertTrue(result is AppResult.Success)
        assertEquals(expected, (result as AppResult.Success).data)
        coVerify(exactly = 1) { homeRepository.getCategories() }
    }

    @Test
    fun `GetOffersUseCase delegates to repository`() = runTest {
        val expected = listOf(Offer("1", "Promo", "50% Off", null))
        coEvery { homeRepository.getOffers() } returns AppResult.Success(expected)

        val result = getOffersUseCase()

        assertTrue(result is AppResult.Success)
        assertEquals(expected, (result as AppResult.Success).data)
        coVerify(exactly = 1) { homeRepository.getOffers() }
    }

    @Test
    fun `GetProductsUseCase delegates to repository with query parameters`() = runTest {
        val expected = listOf(Product("1", "Shoes", null, 100.0, null, null, null, 4.0f))
        coEvery { homeRepository.getProducts(searchTerm = "Shoes", page = 1, pageSize = 10) } returns AppResult.Success(expected)

        val result = getProductsUseCase(searchTerm = "Shoes", page = 1, pageSize = 10)

        assertTrue(result is AppResult.Success)
        assertEquals(expected, (result as AppResult.Success).data)
        coVerify(exactly = 1) { homeRepository.getProducts(searchTerm = "Shoes", page = 1, pageSize = 10) }
    }
}
