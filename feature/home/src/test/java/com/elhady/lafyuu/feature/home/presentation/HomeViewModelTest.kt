package com.elhady.lafyuu.feature.home.presentation

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.designsystem.components.element.TabBarItem
import com.elhady.lafyuu.core.designsystem.icons.Search
import com.elhady.lafyuu.feature.home.domain.model.Category
import com.elhady.lafyuu.feature.home.domain.model.HomeContent
import com.elhady.lafyuu.feature.home.domain.model.Offer
import com.elhady.lafyuu.feature.home.domain.model.Product
import com.elhady.lafyuu.feature.home.domain.usecase.GetHomeContentUseCase
import com.elhady.lafyuu.feature.home.domain.usecase.SearchProductsUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
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
    private val getHomeContentUseCase: GetHomeContentUseCase = mockk()
    private val searchProductsUseCase: SearchProductsUseCase = mockk()

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        coEvery { getHomeContentUseCase() } returns AppResult.Success(
            HomeContent(
                categories = listOf(Category("1", "Clothes", null, null)),
                offers = listOf(Offer("1", "Summer Sale", "50% Off", null)),
                flashSaleProducts = listOf(Product("1", "Nike Air", null, 100.0, 120.0, 20.0, "20% OFF", 4.5f)),
                megaSaleProducts = listOf(Product("1", "Nike Air", null, 100.0, 120.0, 20.0, "20% OFF", 4.5f)),
                recommendedProducts = listOf(Product("1", "Nike Air", null, 100.0, 120.0, 20.0, "20% OFF", 4.5f))
            )
        )
        coEvery { searchProductsUseCase(any()) } returns AppResult.Success(emptyList())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init loads home content into UiState`() = runTest {
        viewModel = HomeViewModel(getHomeContentUseCase, searchProductsUseCase)
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
    fun `init sets errorMessage when GetHomeContentUseCase returns Error`() = runTest {
        coEvery { getHomeContentUseCase() } returns AppResult.Error(message = "Network error")

        viewModel = HomeViewModel(getHomeContentUseCase, searchProductsUseCase)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("Network error", state.errorMessage)
    }

    @Test
    fun `non-blank query immediately sets isSearching true before debounce`() = runTest {
        viewModel = HomeViewModel(getHomeContentUseCase, searchProductsUseCase)
        advanceUntilIdle()

        viewModel.onEvent(HomeUiEvent.SearchQueryChanged("Puma"))

        val state = viewModel.uiState.value
        assertEquals("Puma", state.searchQuery)
        assertTrue(state.isSearching)
    }

    @Test
    fun `blank query clears search state immediately`() = runTest {
        viewModel = HomeViewModel(getHomeContentUseCase, searchProductsUseCase)
        advanceUntilIdle()

        viewModel.onEvent(HomeUiEvent.SearchQueryChanged("Puma"))
        viewModel.onEvent(HomeUiEvent.SearchQueryChanged("   "))

        val state = viewModel.uiState.value
        assertEquals("", state.searchQuery)
        assertFalse(state.isSearching)
        assertTrue(state.searchResults.isEmpty())
    }

    @Test
    fun `onSearchQueryChange debounces and updates search state with results`() = runTest {
        viewModel = HomeViewModel(getHomeContentUseCase, searchProductsUseCase)
        advanceUntilIdle()

        val searchProducts = listOf(Product("2", "Adidas Ultra", null, 90.0, null, null, null, 4.0f))
        coEvery { searchProductsUseCase("Adidas") } returns AppResult.Success(searchProducts)

        viewModel.onEvent(HomeUiEvent.SearchQueryChanged("Adidas"))
        advanceTimeBy(350L)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("Adidas", state.searchQuery)
        assertFalse(state.isSearching)
        assertEquals(1, state.searchResults.size)
        assertEquals("Adidas Ultra", state.searchResults.first().name)
    }

    @Test
    fun `rapid typing within debounce period only triggers search for the last query`() = runTest {
        viewModel = HomeViewModel(getHomeContentUseCase, searchProductsUseCase)
        advanceUntilIdle()

        coEvery { searchProductsUseCase(any()) } returns AppResult.Success(emptyList())

        viewModel.onEvent(HomeUiEvent.SearchQueryChanged("N"))
        advanceTimeBy(100L)
        viewModel.onEvent(HomeUiEvent.SearchQueryChanged("Ni"))
        advanceTimeBy(100L)
        viewModel.onEvent(HomeUiEvent.SearchQueryChanged("Nik"))
        advanceTimeBy(350L)
        advanceUntilIdle()

        coVerify(exactly = 1) { searchProductsUseCase("Nik") }
        coVerify(exactly = 0) { searchProductsUseCase("N") }
        coVerify(exactly = 0) { searchProductsUseCase("Ni") }
    }

    @Test
    fun `successful search with zero results sets isSearching false and searchResults empty`() = runTest {
        viewModel = HomeViewModel(getHomeContentUseCase, searchProductsUseCase)
        advanceUntilIdle()

        coEvery { searchProductsUseCase("Unknown") } returns AppResult.Success(emptyList())

        viewModel.onEvent(HomeUiEvent.SearchQueryChanged("Unknown"))
        advanceTimeBy(350L)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("Unknown", state.searchQuery)
        assertFalse(state.isSearching)
        assertTrue(state.searchResults.isEmpty())
    }

    @Test
    fun `search error sets isSearching false and searchResults empty`() = runTest {
        viewModel = HomeViewModel(getHomeContentUseCase, searchProductsUseCase)
        advanceUntilIdle()

        coEvery { searchProductsUseCase("ErrorQuery") } returns AppResult.Error("Network error")

        viewModel.onEvent(HomeUiEvent.SearchQueryChanged("ErrorQuery"))
        advanceTimeBy(350L)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("ErrorQuery", state.searchQuery)
        assertFalse(state.isSearching)
        assertTrue(state.searchResults.isEmpty())
    }

    @Test
    fun `clear search while debounce is pending cancels search and resets state`() = runTest {
        viewModel = HomeViewModel(getHomeContentUseCase, searchProductsUseCase)
        advanceUntilIdle()

        viewModel.onEvent(HomeUiEvent.SearchQueryChanged("Pending"))
        advanceTimeBy(100L)

        viewModel.onEvent(HomeUiEvent.SearchClicked)
        advanceTimeBy(350L)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("", state.searchQuery)
        assertFalse(state.isSearching)
        assertTrue(state.searchResults.isEmpty())
        coVerify(exactly = 0) { searchProductsUseCase("Pending") }
    }

    @Test
    fun `clear search while request is in flight resets state and cancels in flight search`() = runTest {
        viewModel = HomeViewModel(getHomeContentUseCase, searchProductsUseCase)
        advanceUntilIdle()

        coEvery { searchProductsUseCase("InFlight") } coAnswers {
            delay(1000L)
            AppResult.Success(listOf(Product("99", "InFlight Item", null, 10.0, null, null, null, 4.0f)))
        }

        viewModel.onEvent(HomeUiEvent.SearchQueryChanged("InFlight"))
        advanceTimeBy(350L) // debounce passes, search invocation starts
        advanceTimeBy(200L) // request in flight

        viewModel.onEvent(HomeUiEvent.SearchClicked)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("", state.searchQuery)
        assertFalse(state.isSearching)
        assertTrue(state.searchResults.isEmpty())
    }

    @Test
    fun `onClearSearch resets search query and search results immediately`() = runTest {
        viewModel = HomeViewModel(getHomeContentUseCase, searchProductsUseCase)
        advanceUntilIdle()

        val searchProducts = listOf(Product("2", "Adidas Ultra", null, 90.0, null, null, null, 4.0f))
        coEvery { searchProductsUseCase("Adidas") } returns AppResult.Success(searchProducts)

        viewModel.onEvent(HomeUiEvent.SearchQueryChanged("Adidas"))
        advanceTimeBy(350L)
        advanceUntilIdle()

        viewModel.onEvent(HomeUiEvent.SearchClicked)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("", state.searchQuery)
        assertFalse(state.isSearching)
        assertTrue(state.searchResults.isEmpty())
    }

    @Test
    fun `FavoriteClicked event toggles product isFavorite state locally`() = runTest {
        viewModel = HomeViewModel(getHomeContentUseCase, searchProductsUseCase)
        advanceUntilIdle()

        viewModel.onEvent(HomeUiEvent.FavoriteClicked("1"))

        val state = viewModel.uiState.value
        assertTrue(state.flashSaleProducts.first().isFavorite)
    }

    @Test
    fun `ProductClicked event emits NavigateToProductDetails effect`() = runTest {
        viewModel = HomeViewModel(getHomeContentUseCase, searchProductsUseCase)
        advanceUntilIdle()

        viewModel.onEvent(HomeUiEvent.ProductClicked("100"))

        val effect = viewModel.effect.first()
        assertTrue(effect is HomeUiEffect.NavigateToProductDetails)
        assertEquals("100", (effect as HomeUiEffect.NavigateToProductDetails).productId)
    }

    @Test
    fun `BottomTabSelected event emits NavigateToTab effect with tab route`() = runTest {
        viewModel = HomeViewModel(getHomeContentUseCase, searchProductsUseCase)
        advanceUntilIdle()

        val tab = TabBarItem("Explore", Search, route = "explore")
        viewModel.onEvent(HomeUiEvent.BottomTabSelected(tab))

        val effect = viewModel.effect.first()
        assertTrue(effect is HomeUiEffect.NavigateToTab)
        assertEquals("explore", (effect as HomeUiEffect.NavigateToTab).tabRoute)
    }
}
