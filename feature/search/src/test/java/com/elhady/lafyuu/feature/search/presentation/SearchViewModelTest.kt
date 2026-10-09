package com.elhady.lafyuu.feature.search.presentation

import androidx.lifecycle.SavedStateHandle
import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.search.domain.model.Category
import com.elhady.lafyuu.feature.search.domain.model.Product
import com.elhady.lafyuu.feature.search.domain.usecase.GetCategoriesUseCase
import com.elhady.lafyuu.feature.search.domain.usecase.SearchProductsUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
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
class SearchViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val searchProductsUseCase: SearchProductsUseCase = mockk(relaxed = true)
    private val getCategoriesUseCase: GetCategoriesUseCase = mockk(relaxed = true)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        coEvery { getCategoriesUseCase() } returns AppResult.Success(listOf(Category("1", "Electronics", "", "")))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `blank query clears products and does not search`() = runTest {
        val viewModel = SearchViewModel(searchProductsUseCase, getCategoriesUseCase, SavedStateHandle())
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(SearchUiEvent.QueryChanged(""))
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state.products.isEmpty())
        assertFalse(state.isLoading)
    }

    @Test
    fun `search submitted triggers use case with query`() = runTest {
        val products = listOf(Product("p1", "Shoes", "", 100.0, null, null, null, null))
        coEvery { searchProductsUseCase(searchTerm = "Shoes", category = null, minPrice = null, maxPrice = null, isInStock = null, sortBy = null, sortOrder = null, page = 1, pageSize = 20) } returns AppResult.Success(products)

        val viewModel = SearchViewModel(searchProductsUseCase, getCategoriesUseCase, SavedStateHandle())
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(SearchUiEvent.SearchSubmitted("Shoes"))
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(1, state.products.size)
        assertEquals("Shoes", state.products[0].name)
    }

    @Test
    fun `apply filters passes correct parameters and resets page`() = runTest {
        val products = listOf(Product("p1", "Phone", "", 500.0, null, null, null, null))
        coEvery { searchProductsUseCase(searchTerm = "Tech", category = "Electronics", minPrice = 100.0, maxPrice = 1000.0, isInStock = true, sortBy = null, sortOrder = null, page = 1, pageSize = 20) } returns AppResult.Success(products)

        val viewModel = SearchViewModel(searchProductsUseCase, getCategoriesUseCase, SavedStateHandle(mapOf("query" to "Tech")))
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(SearchUiEvent.FilterClicked)
        viewModel.onEvent(SearchUiEvent.CategoryFilterChanged("Electronics"))
        viewModel.onEvent(SearchUiEvent.PriceRangeChanged(100.0, 1000.0))
        viewModel.onEvent(SearchUiEvent.InStockChanged(true))
        viewModel.onEvent(SearchUiEvent.ApplyFilters)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("Electronics", state.category)
        assertEquals(100.0, state.minPrice)
        assertEquals(1000.0, state.maxPrice)
        assertEquals(true, state.isInStock)
        assertEquals(1, state.page)
    }
}
