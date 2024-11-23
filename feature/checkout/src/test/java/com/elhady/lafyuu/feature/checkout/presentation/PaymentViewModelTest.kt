package com.elhady.lafyuu.feature.checkout.presentation

import androidx.lifecycle.SavedStateHandle
import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.common.result.AppError
import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.feature.checkout.domain.model.CheckoutResult
import com.elhady.lafyuu.feature.checkout.domain.model.OrderSummary
import com.elhady.lafyuu.feature.checkout.domain.model.PaymentMethod
import com.elhady.lafyuu.feature.checkout.domain.repository.CheckoutCartGateway
import com.elhady.lafyuu.feature.checkout.domain.usecase.CheckoutUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PaymentViewModelTest {

    private val checkoutUseCase: CheckoutUseCase = mockk()
    private val cartGateway: CheckoutCartGateway = mockk(relaxed = true)

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        coEvery { cartGateway.getOrderSummary() } returns
                AppResult.Success(OrderSummary(total = 100.0, couponCode = "SAVE10"))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel(addressId: String = "a-1") = PaymentViewModel(
        checkoutUseCase,
        cartGateway,
        SavedStateHandle(mapOf(PaymentViewModel.SHIPPING_ADDRESS_ID_ARG to addressId))
    )

    @Test
    fun `loads the total and the applied coupon`() = runTest {
        val state = createViewModel().uiState.value

        assertEquals(100.0, state.total!!, 0.001)
        assertEquals("SAVE10", state.couponCode)
        assertEquals(false, state.isLoading)
    }

    @Test
    fun `pay sends the coupon and the chosen method then completes and refreshes the cart`() = runTest {
        coEvery { checkoutUseCase("a-1", PaymentMethod.CASH_ON_DELIVERY, "SAVE10") } returns
                AppResult.Success(CheckoutResult.Completed("Paid successfully"))
        val viewModel = createViewModel()

        viewModel.onIntent(PaymentUiIntent.MethodSelected(PaymentMethod.CASH_ON_DELIVERY))
        viewModel.onIntent(PaymentUiIntent.PayClicked)

        val stage = viewModel.uiState.value.stage as PaymentStage.Completed
        assertNotNull(stage.message)
        assertNull(viewModel.uiState.value.urlToOpen)
        coVerify(exactly = 1) { cartGateway.refreshCart() }
    }

    @Test
    fun `payment url is kept in the state and the order is not placed twice`() = runTest {
        coEvery { checkoutUseCase(any(), any(), any()) } returns
                AppResult.Success(CheckoutResult.PaymentRequired("https://pay.test/1", "Pay now"))
        val viewModel = createViewModel()

        viewModel.onIntent(PaymentUiIntent.PayClicked)
        viewModel.onIntent(PaymentUiIntent.PayClicked)

        val state = viewModel.uiState.value
        assertEquals(PaymentStage.AwaitingPayment("https://pay.test/1"), state.stage)
        assertEquals("https://pay.test/1", state.urlToOpen)
        coVerify(exactly = 1) { checkoutUseCase(any(), any(), any()) }

        viewModel.onIntent(PaymentUiIntent.UrlOpened)
        assertNull(viewModel.uiState.value.urlToOpen)

        viewModel.onIntent(PaymentUiIntent.OpenPaymentPageClicked)
        assertEquals("https://pay.test/1", viewModel.uiState.value.urlToOpen)
    }

    @Test
    fun `a second pay while the first one is running is ignored`() = runTest {
        val gate = CompletableDeferred<Unit>()
        coEvery { checkoutUseCase(any(), any(), any()) } coAnswers {
            gate.await()
            AppResult.Success(CheckoutResult.Completed("Done"))
        }
        val viewModel = createViewModel()

        viewModel.onIntent(PaymentUiIntent.PayClicked)
        viewModel.onIntent(PaymentUiIntent.PayClicked)
        gate.complete(Unit)

        coVerify(exactly = 1) { checkoutUseCase(any(), any(), any()) }
    }

    @Test
    fun `failed checkout shows a snackbar and the user can try again`() = runTest {
        coEvery { checkoutUseCase(any(), any(), any()) } returns AppResult.Failure(AppError.NoInternet)
        val viewModel = createViewModel()
        val effects = mutableListOf<PaymentUiEffect>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiEffect.toList(effects)
        }

        viewModel.onIntent(PaymentUiIntent.PayClicked)

        assertEquals(PaymentStage.Selecting, viewModel.uiState.value.stage)
        assertEquals(false, viewModel.uiState.value.isCheckingOut)
        assertEquals(AlertType.Error, (effects.single() as PaymentUiEffect.ShowSnackbar).type)
        coVerify(exactly = 0) { cartGateway.refreshCart() }
    }

    @Test
    fun `without a total the user can not pay and retry loads it`() = runTest {
        coEvery { cartGateway.getOrderSummary() } returns AppResult.Failure(AppError.NoInternet)
        val viewModel = createViewModel()
        assertNotNull(viewModel.uiState.value.loadError)

        viewModel.onIntent(PaymentUiIntent.PayClicked)
        coVerify(exactly = 0) { checkoutUseCase(any(), any(), any()) }

        coEvery { cartGateway.getOrderSummary() } returns AppResult.Success(OrderSummary(50.0, null))
        viewModel.onIntent(PaymentUiIntent.RetryClicked)

        assertNull(viewModel.uiState.value.loadError)
        assertEquals(50.0, viewModel.uiState.value.total!!, 0.001)
    }

    @Test
    fun `the method can not change after the order was placed`() = runTest {
        coEvery { checkoutUseCase(any(), any(), any()) } returns
                AppResult.Success(CheckoutResult.Completed("Done"))
        val viewModel = createViewModel()
        viewModel.onIntent(PaymentUiIntent.PayClicked)

        viewModel.onIntent(PaymentUiIntent.MethodSelected(PaymentMethod.PAYPAL))

        assertTrue(viewModel.uiState.value.selectedMethod == PaymentMethod.CREDIT_CARD)
    }
}