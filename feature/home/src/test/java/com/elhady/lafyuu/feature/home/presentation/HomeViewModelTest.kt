package com.elhady.lafyuu.feature.home.presentation

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.home.domain.model.Category
import com.elhady.lafyuu.feature.home.domain.model.Offer
import com.elhady.lafyuu.feature.home.domain.model.Product
import com.elhady.lafyuu.feature.home.domain.usecase.GetCategoriesUseCase
import com.elhady.lafyuu.feature.home.domain.usecase.GetOffersUseCase
import com.elhady.lafyuu.feature.home.domain.usecase.GetProductsUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val getCategoriesUseCase: GetCategoriesUseCase = mockk()
    private val getOffersUseCase: GetOffersUseCase = mockk()
    private val getProductsUseCase: GetProductsUseCase = mockk()

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        coEvery { getCategoriesUseCase() } returns AppResult.Success(listOf(Category("1", "Clothes", null, null)))
        coEvery { getOffersUseCase() } returns AppResult.Success(listOf(Offer("1", "Summer Sale", "50% Off", null)))
        coEvery { getProductsUseCase(any(), any(), any(), any(), any(), any()) } returns AppResult.Success(
            listOf(Product("1", "Nike Air", null, 100.0, 120.0, 20.0, "20% OFF", 4.5f))
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init loads categories, offers, and products into UiState`() = runTest {
        viewModel = HomeViewModel(getCategoriesUseCase, getOffersUseCase, getProductsUseCase)
        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertFalse(state.isLoading)
        assertEquals(1, state.categories.size)
        assertEquals("Clothes", state.categories.first().name)
        assertEquals(1, state.offers.size)
        assertEquals("Summer Sale", state.offers.first().name)
        assertEquals(1, state.flashSaleProducts.size)
    }

    @Test
    fun `onSearchQueryChange updates search state and fetches search results`() = runTest {
        viewModel = HomeViewModel(getCategoriesUseCase, getOffersUseCase, getProductsUseCase)
        advanceUntilIdle()

        val searchProducts = listOf(Product("2", "Adidas Ultra", null, 90.0, null, null, null, 4.0f))
        coEvery { getProductsUseCase(searchTerm = "Adidas", pageSize = 20) } returns AppResult.Success(searchProducts)

        viewModel.onEvent(HomeUiEvent.SearchQueryChanged("Adidas"))
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("Adidas", state.searchQuery)
        assertTrue(state.isSearching)
        assertEquals(1, state.searchResults.size)
        assertEquals("Adidas Ultra", state.searchResults.first().name)
    }

    @Test
    fun `onClearSearch resets search query and search results`() = runTest {
        viewModel = HomeViewModel(getCategoriesUseCase, getOffersUseCase, getProductsUseCase)
        advanceUntilIdle()

        viewModel.onEvent(HomeUiEvent.SearchQueryChanged("Shoes"))
        advanceUntilIdle()

        viewModel.onEvent(HomeUiEvent.ClearSearchClicked)

        val state = viewModel.uiState.value
        assertEquals("", state.searchQuery)
        assertFalse(state.isSearching)
        assertTrue(state.searchResults.isEmpty())
    }

    @Test
    fun `ProductClicked event emits NavigateToProductDetails effect`() = runTest {
        viewModel = HomeViewModel(getCategoriesUseCase, getOffersUseCase, getProductsUseCase)
        advanceUntilIdle()

        viewModel.onEvent(HomeUiEvent.ProductClicked("100"))

        val effect = viewModel.effect.first()
        assertTrue(effect is HomeUiEffect.NavigateToProductDetails)
        assertEquals("100", (effect as HomeUiEffect.NavigateToProductDetails).productId)
    }
}
