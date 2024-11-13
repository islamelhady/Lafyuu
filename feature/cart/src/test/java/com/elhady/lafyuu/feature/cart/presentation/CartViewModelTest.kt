package com.elhady.lafyuu.feature.cart.presentation

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.common.result.AppError
import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.feature.cart.domain.model.Cart
import com.elhady.lafyuu.feature.cart.domain.model.CartItem
import com.elhady.lafyuu.feature.cart.domain.repository.CartFavoritesGateway
import com.elhady.lafyuu.feature.cart.domain.usecase.ApplyCouponUseCase
import com.elhady.lafyuu.feature.cart.domain.usecase.DecreaseCartItemUseCase
import com.elhady.lafyuu.feature.cart.domain.usecase.GetCartUseCase
import com.elhady.lafyuu.feature.cart.domain.usecase.IncreaseCartItemQuantityUseCase
import com.elhady.lafyuu.feature.cart.domain.usecase.ObserveCartUseCase
import com.elhady.lafyuu.feature.cart.domain.usecase.RemoveCartItemUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
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
class CartViewModelTest {

    private val getCartUseCase: GetCartUseCase = mockk()
    private val observeCartUseCase: ObserveCartUseCase = mockk()
    private val increaseUseCase: IncreaseCartItemQuantityUseCase = mockk()
    private val decreaseUseCase: DecreaseCartItemUseCase = mockk()
    private val removeUseCase: RemoveCartItemUseCase = mockk()
    private val applyCouponUseCase: ApplyCouponUseCase = mockk()
    private val favoritesGateway: CartFavoritesGateway = mockk(relaxed = true)

    private val cart = Cart(
        cartId = "c-100",
        items = listOf(
            CartItem(
                itemId = "i-1", productId = "p-1", productName = "Running Shoes",
                productCoverUrl = "", productStock = 10, weightInGrams = 500.0,
                quantity = 1, discountPercentage = 0.0, basePricePerUnit = 100.0,
                finalPricePerUnit = 100.0, totalPrice = 100.0
            )
        )
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        coEvery { getCartUseCase() } returns AppResult.Success(cart)
        every { observeCartUseCase() } returns flowOf(cart)
        every { favoritesGateway.observeFavoriteProductIds() } returns flowOf(emptySet())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel() = CartViewModel(
        getCartUseCase,
        observeCartUseCase,
        increaseUseCase,
        decreaseUseCase,
        removeUseCase,
        applyCouponUseCase,
        favoritesGateway
    )

    @Test
    fun `loads cart on init`() = runTest {
        val state = createViewModel().uiState.value

        assertFalse(state.isLoading)
        assertEquals(1, state.items.size)
        assertEquals("Running Shoes", state.items[0].name)
        assertEquals("\$100.00", state.cart?.summary?.total)
    }

    @Test
    fun `failed first load shows an error and no cart`() = runTest {
        coEvery { getCartUseCase() } returns AppResult.Failure(AppError.NoInternet)
        every { observeCartUseCase() } returns flowOf(null)

        val state = createViewModel().uiState.value

        assertFalse(state.isLoading)
        assertNotNull(state.error)
        assertTrue(state.items.isEmpty())
    }

    @Test
    fun `CheckoutClicked emits NavigateToCheckout`() = runTest {
        val viewModel = createViewModel()
        val effects = mutableListOf<CartUiEffect>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiEffect.toList(effects)
        }

        viewModel.onIntent(CartUiIntent.CheckoutClicked)

        assertEquals(listOf<CartUiEffect>(CartUiEffect.NavigateToCheckout), effects)
    }

    @Test
    fun `IncreaseQuantity failure emits an error snackbar`() = runTest {
        coEvery { increaseUseCase("i-1") } returns AppResult.Failure(AppError.OutOfStock(9))
        val viewModel = createViewModel()
        val effects = mutableListOf<CartUiEffect>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiEffect.toList(effects)
        }

        viewModel.onIntent(CartUiIntent.IncreaseQuantity("i-1"))

        val effect = effects.single() as CartUiEffect.ShowSnackbar
        assertEquals(AlertType.Error, effect.type)
        assertTrue(viewModel.uiState.value.updatingItemIds.isEmpty())
    }

    @Test
    fun `blank coupon sets couponError without calling the use case`() = runTest {
        val viewModel = createViewModel()

        viewModel.onIntent(CartUiIntent.ApplyCouponClicked)

        assertNotNull(viewModel.uiState.value.couponError)
        coVerify(exactly = 0) { applyCouponUseCase(any()) }
    }

    @Test
    fun `invalid coupon is shown inline`() = runTest {
        coEvery { applyCouponUseCase("BAD") } returns AppResult.Failure(AppError.Validation("bad"))
        val viewModel = createViewModel()

        viewModel.onIntent(CartUiIntent.CouponCodeChanged("BAD"))
        viewModel.onIntent(CartUiIntent.ApplyCouponClicked)

        val state = viewModel.uiState.value
        assertNotNull(state.couponError)
        assertFalse(state.isApplyingCoupon)
    }

    @Test
    fun `favorite click stores a snapshot of the cart item`() = runTest {
        val viewModel = createViewModel()

        viewModel.onIntent(CartUiIntent.FavoriteClicked("p-1"))

        coVerify(exactly = 1) { favoritesGateway.addFavorite(cart.items.first()) }
    }
}