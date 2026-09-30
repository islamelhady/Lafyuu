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
    private lateinit var getHomeContentUseCase: GetHomeContentUseCase
    private lateinit var searchProductsUseCase: SearchProductsUseCase

    @Before
    fun setUp() {
        getCategoriesUseCase = GetCategoriesUseCase(homeRepository)
        getOffersUseCase = GetOffersUseCase(homeRepository)
        getProductsUseCase = GetProductsUseCase(homeRepository)
        getHomeContentUseCase = GetHomeContentUseCase(homeRepository)
        searchProductsUseCase = SearchProductsUseCase(homeRepository)
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

    @Test
    fun `GetHomeContentUseCase orchestrates requests, deduplicates products, and returns HomeContent`() = runTest {
        val categories = listOf(Category("1", "Bags", null, null))
        val offers = listOf(Offer("10", "Flash Sale", "50%", null))
        val product1 = Product("p1", "Item 1", null, 10.0, null, null, null, 4.0f)
        val product2 = Product("p2", "Item 2", null, 20.0, null, null, null, 4.5f)

        coEvery { homeRepository.getCategories() } returns AppResult.Success(categories)
        coEvery { homeRepository.getOffers() } returns AppResult.Success(offers)
        coEvery { homeRepository.getProducts(page = 1, pageSize = 10) } returns AppResult.Success(listOf(product1))
        coEvery { homeRepository.getProducts(page = 2, pageSize = 10) } returns AppResult.Success(listOf(product2))
        coEvery { homeRepository.getProducts(page = 1, pageSize = 20) } returns AppResult.Success(listOf(product1, product2))

        val result = getHomeContentUseCase()

        assertTrue(result is AppResult.Success)
        val content = (result as AppResult.Success).data
        assertEquals(categories, content.categories)
        assertEquals(offers, content.offers)
        assertEquals(listOf(product1), content.flashSaleProducts)
        assertEquals(listOf(product2), content.megaSaleProducts)
        assertEquals(listOf(product1, product2), content.recommendedProducts)
    }

    @Test
    fun `GetHomeContentUseCase applies fallback when section products are empty`() = runTest {
        val product1 = Product("p1", "Item 1", null, 10.0, null, null, null, 4.0f)

        coEvery { homeRepository.getCategories() } returns AppResult.Success(emptyList())
        coEvery { homeRepository.getOffers() } returns AppResult.Success(emptyList())
        coEvery { homeRepository.getProducts(page = 1, pageSize = 10) } returns AppResult.Success(emptyList())
        coEvery { homeRepository.getProducts(page = 2, pageSize = 10) } returns AppResult.Success(emptyList())
        coEvery { homeRepository.getProducts(page = 1, pageSize = 20) } returns AppResult.Success(listOf(product1))

        val result = getHomeContentUseCase()

        assertTrue(result is AppResult.Success)
        val content = (result as AppResult.Success).data
        assertEquals(listOf(product1), content.flashSaleProducts)
        assertEquals(listOf(product1), content.megaSaleProducts)
        assertEquals(listOf(product1), content.recommendedProducts)
    }

    @Test
    fun `GetHomeContentUseCase returns Success with categories when categories succeed but product requests fail`() = runTest {
        val categories = listOf(Category("1", "Bags", null, null))

        coEvery { homeRepository.getCategories() } returns AppResult.Success(categories)
        coEvery { homeRepository.getOffers() } returns AppResult.Error(message = "Network Error")
        coEvery { homeRepository.getProducts(any(), any(), any(), any(), any(), any()) } returns AppResult.Error(message = "Network Error")

        val result = getHomeContentUseCase()

        assertTrue(result is AppResult.Success)
        val content = (result as AppResult.Success).data
        assertEquals(categories, content.categories)
        assertTrue(content.offers.isEmpty())
        assertTrue(content.flashSaleProducts.isEmpty())
        assertTrue(content.megaSaleProducts.isEmpty())
        assertTrue(content.recommendedProducts.isEmpty())
    }

    @Test
    fun `GetHomeContentUseCase returns Success with products when offers fail but product requests succeed`() = runTest {
        val categories = listOf(Category("1", "Shoes", null, null))
        val product1 = Product("p1", "Item 1", null, 10.0, null, null, null, 4.0f)

        coEvery { homeRepository.getCategories() } returns AppResult.Success(categories)
        coEvery { homeRepository.getOffers() } returns AppResult.Error(message = "Network Error")
        coEvery { homeRepository.getProducts(page = 1, pageSize = 10) } returns AppResult.Success(listOf(product1))
        coEvery { homeRepository.getProducts(page = 2, pageSize = 10) } returns AppResult.Success(emptyList())
        coEvery { homeRepository.getProducts(page = 1, pageSize = 20) } returns AppResult.Success(listOf(product1))

        val result = getHomeContentUseCase()

        assertTrue(result is AppResult.Success)
        val content = (result as AppResult.Success).data
        assertEquals(categories, content.categories)
        assertTrue(content.offers.isEmpty())
        assertEquals(listOf(product1), content.flashSaleProducts)
        assertEquals(listOf(product1), content.recommendedProducts)
    }

    @Test
    fun `GetHomeContentUseCase applies fallback when one product section fails but others succeed`() = runTest {
        val categories = listOf(Category("1", "Fashion", null, null))
        val product1 = Product("p1", "Item 1", null, 10.0, null, null, null, 4.0f)

        coEvery { homeRepository.getCategories() } returns AppResult.Success(categories)
        coEvery { homeRepository.getOffers() } returns AppResult.Success(emptyList())
        coEvery { homeRepository.getProducts(page = 1, pageSize = 10) } returns AppResult.Error(message = "Flash sale error")
        coEvery { homeRepository.getProducts(page = 2, pageSize = 10) } returns AppResult.Success(emptyList())
        coEvery { homeRepository.getProducts(page = 1, pageSize = 20) } returns AppResult.Success(listOf(product1))

        val result = getHomeContentUseCase()

        assertTrue(result is AppResult.Success)
        val content = (result as AppResult.Success).data
        assertEquals(categories, content.categories)
        assertEquals(listOf(product1), content.flashSaleProducts)
        assertEquals(listOf(product1), content.recommendedProducts)
    }

    @Test
    fun `GetHomeContentUseCase returns Error when all major sections fail`() = runTest {
        coEvery { homeRepository.getCategories() } returns AppResult.Error(message = "Network Error")
        coEvery { homeRepository.getOffers() } returns AppResult.Error(message = "Network Error")
        coEvery { homeRepository.getProducts(any(), any(), any(), any(), any(), any()) } returns AppResult.Error(message = "Network Error")

        val result = getHomeContentUseCase()

        assertTrue(result is AppResult.Error)
        assertEquals("Network Error", (result as AppResult.Error).message)
    }

    @Test
    fun `SearchProductsUseCase returns empty Success for blank query without calling repository`() = runTest {
        val result = searchProductsUseCase("  ")

        assertTrue(result is AppResult.Success)
        assertTrue((result as AppResult.Success).data.isEmpty())
        coVerify(exactly = 0) { homeRepository.getProducts(any(), any(), any(), any(), any(), any()) }
    }

    @Test
    fun `SearchProductsUseCase delegates non-blank query to repository`() = runTest {
        val expected = listOf(Product("p1", "Nike", null, 100.0, null, null, null, 5.0f))
        coEvery { homeRepository.getProducts(searchTerm = "Nike", pageSize = 20) } returns AppResult.Success(expected)

        val result = searchProductsUseCase("Nike")

        assertTrue(result is AppResult.Success)
        assertEquals(expected, (result as AppResult.Success).data)
        coVerify(exactly = 1) { homeRepository.getProducts(searchTerm = "Nike", pageSize = 20) }
    }
}
