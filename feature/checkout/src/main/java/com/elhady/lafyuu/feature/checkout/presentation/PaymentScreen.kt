package com.elhady.lafyuu.feature.checkout.presentation

import Left
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.elhady.lafyuu.core.designsystem.components.appbar.SingleTopAppBar
import com.elhady.lafyuu.core.designsystem.components.button.DefaultButton
import com.elhady.lafyuu.core.designsystem.components.element.LafyuuScaffold
import com.elhady.lafyuu.core.designsystem.components.list.SingleListItem
import com.elhady.lafyuu.core.designsystem.components.other.LafyuuSnackBarVisuals
import com.elhady.lafyuu.core.designsystem.icons.Bank
import com.elhady.lafyuu.core.designsystem.icons.CreditCard
import com.elhady.lafyuu.core.designsystem.icons.Paypal
import com.elhady.lafyuu.core.designsystem.icons.Transaction
import com.elhady.lafyuu.core.designsystem.theme.Theme

@Composable
fun PaymentRoute(
    onNavigateBack: () -> Unit,
    onNavigateToSuccess: () -> Unit,
    onOpenUrl: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PaymentViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                PaymentUiEffect.NavigateBack -> onNavigateBack()
                is PaymentUiEffect.OpenExternalUrl -> onOpenUrl(effect.url)
                is PaymentUiEffect.ShowSnackBar -> {
                    snackBarHostState.showSnackbar(
                        visuals = LafyuuSnackBarVisuals(
                            message = effect.message,
                            type = effect.type
                        )
                    )
                }
            }
        }
    }

    if (uiState.isSuccess) {
        CheckoutSuccessScreen(
            message = uiState.checkoutResult?.message ?: "Success!",
            onDismiss = {
                viewModel.onEvent(PaymentUiEvent.DismissSuccess)
                onNavigateToSuccess()
            }
        )
    } else {
        PaymentScreen(
            uiState = uiState,
            snackBarHostState = snackBarHostState,
            onEvent = viewModel::onEvent,
            modifier = modifier
        )
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun PaymentScreen(
    uiState: PaymentUiState,
    snackBarHostState: SnackbarHostState,
    onEvent: (PaymentUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    LafyuuScaffold(
        snackbarHostState = snackBarHostState,
        topBar = {
            SingleTopAppBar(
                title = "Payment",
                leadingIcon = Left,
                onLeadingClick = { onEvent(PaymentUiEvent.BackClicked) }
            )
        },
        bottomBar = {
            DefaultButton(
                caption = "Pay $${String.format("%.2f", uiState.checkoutTotal)}",
                isLoading = uiState.isCheckingOut,
                onClick = { onEvent(PaymentUiEvent.PayClicked(null)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Theme.space.large)
            )
        }
    ) {
        LazyColumn(
            modifier = modifier.fillMaxWidth()
        ) {
            items(uiState.paymentMethods) { method ->
                val leadingIcon = when (method) {
                    "Credit Card" -> CreditCard
                    "Paypal" -> Paypal
                    "Bank Transfer" -> Bank
                    else -> Transaction
                }
                val isSelected = method == uiState.selectedPaymentMethod
                SingleListItem(
                    title = method,
                    leadingIcon = leadingIcon,
                    leadingIconTint = Theme.color.blue,
                    onClick = { onEvent(PaymentUiEvent.SelectPaymentMethod(method)) },
                    isSelected = isSelected
                )
            }
        }
    }
}
