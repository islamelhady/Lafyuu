package com.elhady.lafyuu.feature.product.presentation

import androidx.lifecycle.SavedStateHandle
import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.product.domain.model.ProductDetails
import com.elhady.lafyuu.feature.product.domain.usecase.AddToCartUseCase
import com.elhady.lafyuu.feature.product.domain.usecase.GetProductDetailsUseCase
import com.elhady.lafyuu.feature.product.domain.usecase.ProductContent
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
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
class ProductDetailsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val getProductDetailsUseCase: GetProductDetailsUseCase = mockk()
    private val addToCartUseCase: AddToCartUseCase = mockk()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loads product details automatically on init if productId present in SavedStateHandle`() = runTest {
        val details = ProductDetails(id = "p-100", name = "Initialized Product", availableSizes = listOf("7", "8"))
        coEvery { getProductDetailsUseCase("p-100") } returns AppResult.Success(
            ProductContent(details = details)
        )

        val savedStateHandle = SavedStateHandle(mapOf("productId" to "p-100"))
        val viewModel = ProductDetailsViewModel(getProductDetailsUseCase, addToCartUseCase, savedStateHandle)

        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("p-100", state.product?.id)
        assertEquals("Initialized Product", state.product?.name)
        assertEquals("7", state.selectedSize)
    }

    @Test
    fun `loadProductDetails failure sets isLoading false and error state`() = runTest {
        coEvery { getProductDetailsUseCase("p-err") } returns AppResult.Error(message = "Network error")

        val savedStateHandle = SavedStateHandle(mapOf("productId" to "p-err"))
        val viewModel = ProductDetailsViewModel(getProductDetailsUseCase, addToCartUseCase, savedStateHandle)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("Network error", state.error)
        assertEquals(null, state.product)
    }

    @Test
    fun `SizeSelected updates selectedSize in state`() = runTest {
        val viewModel = ProductDetailsViewModel(getProductDetailsUseCase, addToCartUseCase, SavedStateHandle())

        viewModel.onEvent(ProductDetailsUiEvent.SizeSelected("8.5"))

        assertEquals("8.5", viewModel.uiState.value.selectedSize)
    }

    @Test
    fun `ColorSelected updates selectedColorHex in state`() = runTest {
        val viewModel = ProductDetailsViewModel(getProductDetailsUseCase, addToCartUseCase, SavedStateHandle())

        viewModel.onEvent(ProductDetailsUiEvent.ColorSelected("#40BFFF"))

        assertEquals("#40BFFF", viewModel.uiState.value.selectedColorHex)
    }

    @Test
    fun `FavoriteToggled toggles favorite boolean`() = runTest {
        val viewModel = ProductDetailsViewModel(getProductDetailsUseCase, addToCartUseCase, SavedStateHandle())

        assertFalse(viewModel.uiState.value.isFavorite)

        viewModel.onEvent(ProductDetailsUiEvent.FavoriteToggled)

        assertTrue(viewModel.uiState.value.isFavorite)
    }

    @Test
    fun `AddToCartClicked calls AddToCartUseCase and emits snackbar effect`() = runTest {
        val details = ProductDetails(id = "p-1", name = "Test Product")
        coEvery { getProductDetailsUseCase("p-1") } returns AppResult.Success(ProductContent(details = details))
        coEvery { addToCartUseCase("p-1", 1) } returns AppResult.Success(Unit)

        val savedStateHandle = SavedStateHandle(mapOf("productId" to "p-1"))
        val viewModel = ProductDetailsViewModel(getProductDetailsUseCase, addToCartUseCase, savedStateHandle)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(ProductDetailsUiEvent.AddToCartClicked)
        testDispatcher.scheduler.advanceUntilIdle()

        val effect = viewModel.uiEffect.first()
        assertTrue(effect is ProductDetailsUiEffect.ShowSnackbar)
        assertEquals("Added to cart successfully!", (effect as ProductDetailsUiEffect.ShowSnackbar).message)
    }

    @Test
    fun `AddToCartClicked failure sets isAddingToCart false and emits error snackbar`() = runTest {
        val details = ProductDetails(id = "p-1", name = "Test Product")
        coEvery { getProductDetailsUseCase("p-1") } returns AppResult.Success(ProductContent(details = details))
        coEvery { addToCartUseCase("p-1", 1) } returns AppResult.Error(message = "Out of stock")

        val savedStateHandle = SavedStateHandle(mapOf("productId" to "p-1"))
        val viewModel = ProductDetailsViewModel(getProductDetailsUseCase, addToCartUseCase, savedStateHandle)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(ProductDetailsUiEvent.AddToCartClicked)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isAddingToCart)
        assertEquals("p-1", state.product?.id)

        val effect = viewModel.uiEffect.first()
        assertTrue(effect is ProductDetailsUiEffect.ShowSnackbar)
        assertEquals("Out of stock", (effect as ProductDetailsUiEffect.ShowSnackbar).message)
    }

    @Test
    fun `RetryClicked triggers product loading again and handles success`() = runTest {
        val details = ProductDetails(id = "p-1", name = "Retry Product")
        coEvery { getProductDetailsUseCase("p-1") } returnsMany listOf(
            AppResult.Error(message = "Failed"),
            AppResult.Success(ProductContent(details = details))
        )

        val savedStateHandle = SavedStateHandle(mapOf("productId" to "p-1"))
        val viewModel = ProductDetailsViewModel(getProductDetailsUseCase, addToCartUseCase, savedStateHandle)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("Failed", viewModel.uiState.value.error)

        viewModel.onEvent(ProductDetailsUiEvent.RetryClicked)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(null, state.error)
        assertEquals("Retry Product", state.product?.name)
    }

    @Test
    fun `RetryClicked handles failed retry exposing expected error`() = runTest {
        coEvery { getProductDetailsUseCase("p-1") } returns AppResult.Error(message = "Still failing")

        val savedStateHandle = SavedStateHandle(mapOf("productId" to "p-1"))
        val viewModel = ProductDetailsViewModel(getProductDetailsUseCase, addToCartUseCase, savedStateHandle)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(ProductDetailsUiEvent.RetryClicked)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("Still failing", state.error)
    }
}
