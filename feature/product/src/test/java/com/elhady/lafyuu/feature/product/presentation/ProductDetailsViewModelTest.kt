package com.elhady.lafyuu.feature.product.presentation

import androidx.lifecycle.SavedStateHandle
import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.common.result.AppError
import com.elhady.lafyuu.feature.product.domain.model.ProductContent
import com.elhady.lafyuu.feature.product.domain.model.ProductDetails
import com.elhady.lafyuu.feature.product.domain.repository.ProductCartGateway
import com.elhady.lafyuu.feature.product.domain.repository.ProductFavoriteGateway
import com.elhady.lafyuu.feature.product.domain.usecase.AddToCartUseCase
import com.elhady.lafyuu.feature.product.domain.usecase.GetProductDetailsUseCase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProductDetailsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val getProductDetailsUseCase: GetProductDetailsUseCase = mockk()
    private val addToCartUseCase: AddToCartUseCase = mockk()
    private val cartGateway: ProductCartGateway = mockk(relaxed = true) {
        every { observeCartItemId(any()) } returns flowOf(null)
    }
    private val favoriteGateway: ProductFavoriteGateway = mockk(relaxed = true) {
        every { isFavorite(any()) } returns flowOf(false)
    }

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
        val details = ProductDetails(
            id = "p-100", productCode = "C-1", name = "Initialized Product", description = "Desc",
            pictureUrls = emptyList(), basePrice = 100.0, finalPrice = 100.0, discountPercentage = 0,
            stock = 10, color = "Black", rating = 5, reviewsCount = 2, sellerId = "s-1"
        )
        coEvery { getProductDetailsUseCase("p-100") } returns AppResult.Success(
            ProductContent(details = details, reviews = emptyList(), recommendedProducts = emptyList())
        )

        val savedStateHandle = SavedStateHandle(mapOf("productId" to "p-100"))
        val viewModel = ProductDetailsViewModel(getProductDetailsUseCase, addToCartUseCase, cartGateway, favoriteGateway, savedStateHandle)

        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("p-100", state.product?.id)
        assertEquals("Initialized Product", state.product?.name)
    }

    @Test
    fun `loadProductDetails failure sets isLoading false and error state`() = runTest {
        coEvery { getProductDetailsUseCase("p-err") } returns AppResult.Failure(AppError.Validation("Network error"))

        val savedStateHandle = SavedStateHandle(mapOf("productId" to "p-err"))
        val viewModel = ProductDetailsViewModel(getProductDetailsUseCase, addToCartUseCase, cartGateway, favoriteGateway, savedStateHandle)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNotNull(state.error)
        assertEquals(null, state.product)
    }
}
