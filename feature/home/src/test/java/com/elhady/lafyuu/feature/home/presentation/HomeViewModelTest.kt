package com.elhady.lafyuu.feature.home.presentation

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.common.result.AppError
import com.elhady.lafyuu.core.designsystem.components.element.TabBarItem
import com.elhady.lafyuu.core.designsystem.icons.Search
import com.elhady.lafyuu.core.ui.text.UiText
import com.elhady.lafyuu.feature.home.domain.model.Category
import com.elhady.lafyuu.feature.home.domain.model.HomeContent
import com.elhady.lafyuu.feature.home.domain.model.HomeSection
import com.elhady.lafyuu.feature.home.domain.model.Offer
import com.elhady.lafyuu.feature.home.domain.model.Product
import com.elhady.lafyuu.feature.home.domain.usecase.GetHomeContentUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val getHomeContent: GetHomeContentUseCase = mockk()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun product(id: String) = Product(id, "Item $id", null, 100.0, 20, 4f)

    private fun content(
        flash: List<Product> = listOf(product("f1")),
        failed: Set<HomeSection> = emptySet()
    ) = HomeContent(
        categories = listOf(Category("1", "Rings", null, null)),
        offers = listOf(Offer("1", "Sale", "50% Off", null)),
        flashSaleProducts = flash,
        megaSaleProducts = listOf(product("m1")),
        recommendedProducts = listOf(product("r1")),
        failedSections = failed
    )

    private fun TestScope.collectEffects(viewModel: HomeViewModel): List<HomeUiEffect> {
        val effects = mutableListOf<HomeUiEffect>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.effect.toList(effects)
        }
        return effects
    }

    @Test
    fun `init loads content and maps it to ui models`() = runTest {
        coEvery { getHomeContent() } returns AppResult.Success(content())

        val viewModel = HomeViewModel(getHomeContent)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(1, state.categories.size)
        assertEquals("$80.00", state.flashSaleProducts.first().price)
        assertTrue(state.failedSections.isEmpty())
    }

    @Test
    fun `home preview shows at most 10 products per sale section`() = runTest {
        coEvery { getHomeContent() } returns
                AppResult.Success(content(flash = (1..12).map { product("f$it") }))

        val viewModel = HomeViewModel(getHomeContent)
        advanceUntilIdle()

        assertEquals(10, viewModel.uiState.value.flashSaleProducts.size)
    }

    @Test
    fun `total failure sets error and empty content`() = runTest {
        coEvery { getHomeContent() } returns AppResult.Failure(AppError.Validation("Network error"))

        val viewModel = HomeViewModel(getHomeContent)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(UiText.Dynamic("Network error"), state.error)
        assertTrue(state.isContentEmpty)
    }

    @Test
    fun `partial failure is exposed in state`() = runTest {
        coEvery { getHomeContent() } returns
                AppResult.Success(content(failed = setOf(HomeSection.Offers)))

        val viewModel = HomeViewModel(getHomeContent)
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.hasLoadProblem)
        assertFalse(viewModel.uiState.value.isContentEmpty)
    }

    @Test
    fun `retry after failure loads content`() = runTest {
        coEvery { getHomeContent() } returnsMany listOf(
            AppResult.Failure(AppError.Validation("Network error")),
            AppResult.Success(content())
        )

        val viewModel = HomeViewModel(getHomeContent)
        advanceUntilIdle()
        viewModel.onIntent(HomeUiIntent.RetryClicked)
        advanceUntilIdle()

        assertNull(viewModel.uiState.value.error)
        assertEquals(1, viewModel.uiState.value.categories.size)
    }

    @Test
    fun `retry while a load is running does not start a second load`() = runTest {
        coEvery { getHomeContent() } coAnswers {
            delay(1_000)
            AppResult.Success(content())
        }

        val viewModel = HomeViewModel(getHomeContent)
        advanceTimeBy(100)
        viewModel.onIntent(HomeUiIntent.RetryClicked)
        advanceUntilIdle()

        coVerify(exactly = 1) { getHomeContent() }
    }

    @Test
    fun `ProductClicked emits NavigateToProductDetails`() = runTest {
        coEvery { getHomeContent() } returns AppResult.Success(content())
        val viewModel = HomeViewModel(getHomeContent)
        val effects = collectEffects(viewModel)
        advanceUntilIdle()

        viewModel.onIntent(HomeUiIntent.ProductClicked("100"))
        advanceUntilIdle()

        assertEquals(listOf<HomeUiEffect>(HomeUiEffect.NavigateToProductDetails("100")), effects)
    }

    @Test
    fun `BottomTabSelected emits NavigateToTab with the tab route`() = runTest {
        coEvery { getHomeContent() } returns AppResult.Success(content())
        val viewModel = HomeViewModel(getHomeContent)
        val effects = collectEffects(viewModel)
        advanceUntilIdle()

        viewModel.onIntent(HomeUiIntent.BottomTabSelected(TabBarItem("Explore", Search, route = "explore")))
        advanceUntilIdle()

        assertEquals(listOf<HomeUiEffect>(HomeUiEffect.NavigateToTab("explore")), effects)
    }
}