package com.elhady.lafyuu.feature.favorites.presentation

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.common.domain.model.ProductDetails
import com.elhady.lafyuu.core.common.domain.repository.ProductRepository
import com.elhady.lafyuu.core.database.repository.FavoritesRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
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
class FavoritesViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val favoritesRepository: FavoritesRepository = mockk(relaxed = true)
    private val productRepository: ProductRepository = mockk()

    private val favoriteIdsFlow = MutableStateFlow<List<String>>(emptyList())

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { favoritesRepository.observeFavoriteIds() } returns favoriteIdsFlow
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `empty database displays empty state`() = runTest {
        favoriteIdsFlow.value = emptyList()
        val viewModel = FavoritesViewModel(favoritesRepository, productRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertTrue(state.favoriteProducts.isEmpty())
        assertEquals(null, state.error)
    }

    @Test
    fun `successful loading displays saved products`() = runTest {
        val details = ProductDetails(id = "p-1", name = "Shoe 1", price = 99.0)
        favoriteIdsFlow.value = listOf("p-1")
        coEvery { productRepository.getProductDetails("p-1") } returns AppResult.Success(details)

        val viewModel = FavoritesViewModel(favoritesRepository, productRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(1, state.favoriteProducts.size)
        assertEquals("Shoe 1", state.favoriteProducts.first().name)
        assertEquals(null, state.error)
    }

    @Test
    fun `complete network failure produces error state`() = runTest {
        favoriteIdsFlow.value = listOf("p-1")
        coEvery { productRepository.getProductDetails("p-1") } returns AppResult.Error("Network failure")

        val viewModel = FavoritesViewModel(favoritesRepository, productRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertTrue(state.favoriteProducts.isEmpty())
        assertEquals("Failed to load favorite products", state.error)
    }

    @Test
    fun `retry reloads saved IDs`() = runTest {
        favoriteIdsFlow.value = listOf("p-1")
        val details = ProductDetails(id = "p-1", name = "Retried Shoe")
        coEvery { productRepository.getProductDetails("p-1") } returnsMany listOf(
            AppResult.Error("Failed"),
            AppResult.Success(details)
        )

        val viewModel = FavoritesViewModel(favoritesRepository, productRepository)
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals("Failed to load favorite products", viewModel.uiState.value.error)

        viewModel.onEvent(FavoritesUiEvent.LoadFavorites)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(1, state.favoriteProducts.size)
        assertEquals("Retried Shoe", state.favoriteProducts.first().name)
        assertEquals(null, state.error)
    }

    @Test
    fun `removing favorite calls repository remove`() = runTest {
        val viewModel = FavoritesViewModel(favoritesRepository, productRepository)
        viewModel.onEvent(FavoritesUiEvent.RemoveFavorite("p-1"))
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { favoritesRepository.removeFavorite("p-1") }
    }
}
