package com.elhady.lafyuu.feature.checkout.presentation

import Left
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.withStarted
import com.elhady.lafyuu.core.designsystem.components.appbar.SingleTopAppBar
import com.elhady.lafyuu.core.designsystem.components.button.DefaultButton
import com.elhady.lafyuu.core.designsystem.components.button.LabelButton
import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.core.designsystem.components.element.InfoStateContent
import com.elhady.lafyuu.core.designsystem.components.element.LafyuuScaffold
import com.elhady.lafyuu.core.designsystem.components.list.SingleListItem
import com.elhady.lafyuu.core.designsystem.components.other.LafyuuSnackBarVisuals
import com.elhady.lafyuu.core.designsystem.components.text.LafyuuText
import com.elhady.lafyuu.core.designsystem.icons.Bank
import com.elhady.lafyuu.core.designsystem.icons.CreditCard
import com.elhady.lafyuu.core.designsystem.icons.Paypal
import com.elhady.lafyuu.core.designsystem.icons.Transaction
import com.elhady.lafyuu.core.designsystem.theme.Theme
import com.elhady.lafyuu.core.ui.text.asString
import com.elhady.lafyuu.feature.checkout.R
import com.elhady.lafyuu.feature.checkout.domain.model.PaymentMethod
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun PaymentRoute(
    onNavigateBack: () -> Unit,
    onNavigateToSuccess: () -> Unit,
    onOpenUrl: (String) -> Unit,
    viewModel: PaymentViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // The payment page lives in the state so it can't be lost: it is opened as soon as
    // the screen is visible, then the state is cleared.
    LaunchedEffect(uiState.urlToOpen) {
        val url = uiState.urlToOpen ?: return@LaunchedEffect
        lifecycleOwner.withStarted { }
        onOpenUrl(url)
        viewModel.onIntent(PaymentUiIntent.UrlOpened)
    }

    LaunchedEffect(viewModel, lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.uiEffect.collect { effect ->
                when (effect) {
                    PaymentUiEffect.NavigateBack -> onNavigateBack()
                    PaymentUiEffect.NavigateToOrders -> onNavigateToSuccess()

                    is PaymentUiEffect.ShowSnackbar -> {
                        snackbarHostState.currentSnackbarData?.dismiss()
                        // separate coroutine: showSnackbar suspends until dismissed
                        launch {
                            snackbarHostState.showSnackbar(
                                visuals = LafyuuSnackBarVisuals(
                                    message = effect.message.asString(context),
                                    type = effect.type
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    PaymentScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onIntent = viewModel::onIntent
    )
}

@Composable
fun PaymentScreen(
    uiState: PaymentUiState,
    snackbarHostState: SnackbarHostState,
    onIntent: (PaymentUiIntent) -> Unit
) {
    val stage = uiState.stage
    val isSelecting = stage == PaymentStage.Selecting

    LafyuuScaffold(
        snackbarHostState = snackbarHostState,
        topBar = {
            if (isSelecting) {
                SingleTopAppBar(
                    title = stringResource(R.string.payment_title),
                    leadingIcon = Left,
                    onLeadingClick = { onIntent(PaymentUiIntent.BackClicked) }
                )
            } else {
                SingleTopAppBar(
                    title = stringResource(R.string.payment_result_title),
                    onLeadingClick = {}
                )
            }
        },
        bottomBar = {
            if (isSelecting && uiState.total != null && uiState.loadError == null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Theme.color.backgroundWhite)
                        .padding(Theme.space.large)
                ) {
                    DefaultButton(
                        caption = stringResource(
                            R.string.payment_pay,
                            String.format(Locale.US, "\$%.2f", uiState.total)
                        ),
                        isLoading = uiState.isCheckingOut,
                        onClick = { onIntent(PaymentUiIntent.PayClicked) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    ) {
        when (stage) {
            is PaymentStage.Completed -> ResultContent(
                title = stringResource(R.string.payment_success_title),
                message = stage.message.asString(),
                primaryCaption = stringResource(R.string.payment_view_orders),
                onPrimaryClick = { onIntent(PaymentUiIntent.ViewOrdersClicked) }
            )

            is PaymentStage.AwaitingPayment -> ResultContent(
                title = stringResource(R.string.payment_pending_title),
                message = stringResource(R.string.payment_pending_message),
                primaryCaption = stringResource(R.string.payment_open_page),
                onPrimaryClick = { onIntent(PaymentUiIntent.OpenPaymentPageClicked) },
                secondaryCaption = stringResource(R.string.payment_view_orders),
                onSecondaryClick = { onIntent(PaymentUiIntent.ViewOrdersClicked) }
            )

            PaymentStage.Selecting -> when {
                uiState.isLoading -> CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Theme.color.blue
                )

                uiState.loadError != null -> InfoStateContent(
                    errorMessage = uiState.loadError.asString(),
                    onRetryClick = { onIntent(PaymentUiIntent.RetryClicked) },
                    errorType = AlertType.Error,
                    modifier = Modifier.align(Alignment.Center)
                )

                else -> LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(PaymentMethod.entries) { method ->
                        val (labelRes, icon) = when (method) {
                            PaymentMethod.CREDIT_CARD -> R.string.payment_method_credit_card to CreditCard
                            PaymentMethod.CASH_ON_DELIVERY -> R.string.payment_method_cash to Transaction
                            PaymentMethod.BANK_TRANSFER -> R.string.payment_method_bank to Bank
                            PaymentMethod.PAYPAL -> R.string.payment_method_paypal to Paypal
                        }
                        SingleListItem(
                            title = stringResource(labelRes),
                            leadingIcon = icon,
                            leadingIconTint = Theme.color.blue,
                            onClick = { onIntent(PaymentUiIntent.MethodSelected(method)) },
                            isSelected = method == uiState.selectedMethod
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ResultContent(
    title: String,
    message: String,
    primaryCaption: String,
    onPrimaryClick: () -> Unit,
    secondaryCaption: String? = null,
    onSecondaryClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(Theme.space.huge),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Theme.space.large)
        ) {
            LafyuuText(
                text = title,
                style = Theme.typography.heading2,
                color = Theme.color.blue
            )
            LafyuuText(
                text = message,
                style = Theme.typography.normalTextRegular,
                color = Theme.color.neutralGrey
            )
            DefaultButton(
                caption = primaryCaption,
                onClick = onPrimaryClick,
                modifier = Modifier.fillMaxWidth()
            )
            if (secondaryCaption != null) {
                LabelButton(
                    caption = secondaryCaption,
                    onClick = onSecondaryClick,
                    contentColor = Theme.color.blue,
                    hasBorder = true,
                    borderColor = Theme.color.blue,
                    style = Theme.typography.largeCaptionBold,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}