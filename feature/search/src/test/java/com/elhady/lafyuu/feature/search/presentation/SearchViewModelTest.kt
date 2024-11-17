package com.elhady.lafyuu.feature.search.presentation

import androidx.lifecycle.SavedStateHandle
import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.common.result.AppError
import com.elhady.lafyuu.feature.search.domain.model.Category
import com.elhady.lafyuu.feature.search.domain.model.Product
import com.elhady.lafyuu.feature.search.domain.model.ProductSort
import com.elhady.lafyuu.feature.search.domain.model.ProductsPage
import com.elhady.lafyuu.feature.search.domain.model.SearchFilters
import com.elhady.lafyuu.feature.search.domain.usecase.GetCategoriesUseCase
import com.elhady.lafyuu.feature.search.domain.usecase.SearchProductsUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    private val searchProductsUseCase: SearchProductsUseCase = mockk()
    private val getCategoriesUseCase: GetCategoriesUseCase = mockk()

    private fun product(id: String) = Product(id = id, name = "Product $id", price = 10.0)
    private fun page(vararg ids: String, hasNext: Boolean = false, number: Int = 1) =
        AppResult.Success(ProductsPage(ids.map(::product), number, hasNext))

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        coEvery { getCategoriesUseCase() } returns AppResult.Success(listOf(Category("1", "Electronics")))
        coEvery { searchProductsUseCase(any(), any()) } returns page("p1")
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel(query: String? = null) = SearchViewModel(
        searchProductsUseCase,
        getCategoriesUseCase,
        SavedStateHandle(if (query == null) emptyMap() else mapOf(SearchViewModel.QUERY_ARG to query))
    )

    @Test
    fun `initial query searches exactly once`() = runTest {
        createViewModel(query = "Tech")

        advanceUntilIdle() // includes the debounce of the initial value

        coVerify(exactly = 1) { searchProductsUseCase(any(), any()) }
    }

    @Test
    fun `typing is debounced into a single search`() = runTest {
        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onIntent(SearchUiIntent.QueryChanged("Sh"))
        advanceTimeBy(100)
        viewModel.onIntent(SearchUiIntent.QueryChanged("Shoes"))
        advanceUntilIdle()

        coVerify(exactly = 1) { searchProductsUseCase(any(), any()) }
        coVerify(exactly = 1) { searchProductsUseCase(SearchFilters(query = "Shoes"), 1) }
        assertEquals(1, viewModel.uiState.value.products.size)
    }

    @Test
    fun `blank query clears results and does not search`() = runTest {
        val viewModel = createViewModel(query = "Shoes")
        advanceUntilIdle()

        viewModel.onIntent(SearchUiIntent.QueryChanged(""))
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state.products.isEmpty())
        assertFalse(state.isLoading)
        coVerify(exactly = 1) { searchProductsUseCase(any(), any()) }
    }

    @Test
    fun `applied filters are sent to the use case from the first page`() = runTest {
        val viewModel = createViewModel(query = "Tech")
        advanceUntilIdle()

        viewModel.onIntent(SearchUiIntent.FilterClicked)
        viewModel.onIntent(SearchUiIntent.CategorySelected("Electronics"))
        viewModel.onIntent(SearchUiIntent.PriceRangeChanged(100, 1000))
        viewModel.onIntent(SearchUiIntent.InStockChanged(true))
        viewModel.onIntent(SearchUiIntent.ApplyFilters)
        advanceUntilIdle()

        coVerify(exactly = 1) {
            searchProductsUseCase(
                SearchFilters(
                    query = "Tech",
                    category = "Electronics",
                    minPrice = 100.0,
                    maxPrice = 1000.0,
                    inStockOnly = true
                ),
                1
            )
        }
        assertEquals(SearchSheet.None, viewModel.uiState.value.sheet)
    }

    @Test
    fun `selecting the same sort twice clears it`() = runTest {
        val viewModel = createViewModel(query = "Tech")
        advanceUntilIdle()

        viewModel.onIntent(SearchUiIntent.SortSelected(ProductSort.PRICE_ASC))
        advanceUntilIdle()
        assertEquals(ProductSort.PRICE_ASC, viewModel.uiState.value.sort)

        viewModel.onIntent(SearchUiIntent.SortSelected(ProductSort.PRICE_ASC))
        advanceUntilIdle()
        assertEquals(null, viewModel.uiState.value.sort)
    }

    @Test
    fun `load more appends the next page without duplicates`() = runTest {
        coEvery { searchProductsUseCase(any(), 1) } returns page("p1", "p2", hasNext = true)
        coEvery { searchProductsUseCase(any(), 2) } returns page("p2", "p3", number = 2)
        val viewModel = createViewModel(query = "Shoes")
        advanceUntilIdle()

        viewModel.onIntent(SearchUiIntent.LoadMore)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(listOf("p1", "p2", "p3"), state.products.map { it.id })
        assertFalse(state.hasNextPage)
        assertFalse(state.isLoadingMore)
    }

    @Test
    fun `first page failure shows an error and retry searches again`() = runTest {
        coEvery { searchProductsUseCase(any(), any()) } returns AppResult.Failure(AppError.NoInternet)
        val viewModel = createViewModel(query = "Shoes")
        advanceUntilIdle()
        assertNotNull(viewModel.uiState.value.error)

        coEvery { searchProductsUseCase(any(), any()) } returns page("p1")
        viewModel.onIntent(SearchUiIntent.RetryClicked)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(null, state.error)
        assertEquals(1, state.products.size)
    }
}