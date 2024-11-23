package com.elhady.lafyuu.feature.checkout.presentation

import Left
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TextButton
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
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.elhady.lafyuu.core.designsystem.components.appbar.ProductTopBar
import com.elhady.lafyuu.core.designsystem.components.button.DefaultButton
import com.elhady.lafyuu.core.designsystem.components.card.AddressCard
import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.core.designsystem.components.element.InfoStateContent
import com.elhady.lafyuu.core.designsystem.components.element.LafyuuScaffold
import com.elhady.lafyuu.core.designsystem.components.other.LafyuuSnackBarVisuals
import com.elhady.lafyuu.core.designsystem.components.text.LafyuuText
import com.elhady.lafyuu.core.designsystem.icons.Plus
import com.elhady.lafyuu.core.designsystem.theme.Theme
import com.elhady.lafyuu.core.ui.text.asString
import com.elhady.lafyuu.feature.checkout.R
import com.elhady.lafyuu.feature.checkout.presentation.model.ShippingAddressUi
import kotlinx.coroutines.launch

@Composable
fun CheckoutRoute(
    onNavigateBack: () -> Unit,
    onNavigateToPayment: (String) -> Unit,
    onNavigateToAddAddress: () -> Unit,
    onNavigateToEditAddress: (String) -> Unit,
    viewModel: CheckoutViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    LifecycleEventEffect(Lifecycle.Event.ON_START) {
        viewModel.onIntent(CheckoutUiIntent.Refresh)
    }

    LaunchedEffect(viewModel, lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.uiEffect.collect { effect ->
                when (effect) {
                    CheckoutUiEffect.NavigateBack -> onNavigateBack()
                    is CheckoutUiEffect.NavigateToPayment ->
                        onNavigateToPayment(effect.shippingAddressId)

                    CheckoutUiEffect.NavigateToAddAddress -> onNavigateToAddAddress()
                    is CheckoutUiEffect.NavigateToEditAddress ->
                        onNavigateToEditAddress(effect.addressId)

                    is CheckoutUiEffect.ShowSnackbar -> {
                        snackbarHostState.currentSnackbarData?.dismiss()
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

    CheckoutScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onIntent = viewModel::onIntent
    )
}

@Composable
fun CheckoutScreen(
    uiState: CheckoutUiState,
    snackbarHostState: SnackbarHostState,
    onIntent: (CheckoutUiIntent) -> Unit
) {
    LafyuuScaffold(
        snackbarHostState = snackbarHostState,
        topBar = {
            ProductTopBar(
                title = stringResource(R.string.checkout_title),
                leadingIcon = Left,
                onLeadingClick = { onIntent(CheckoutUiIntent.BackClicked) },
                trailingIcon = Plus,
                onTrailingClick = { onIntent(CheckoutUiIntent.AddAddressClicked) }
            )
        },
        bottomBar = {
            if (uiState.addresses.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Theme.color.backgroundWhite)
                        .padding(Theme.space.large)
                ) {
                    DefaultButton(
                        caption = stringResource(R.string.checkout_next),
                        isLoading = false,
                        onClick = { onIntent(CheckoutUiIntent.NextClicked) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    ) {
        when {
            uiState.isLoading -> CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Theme.color.blue
            )

            uiState.error != null -> InfoStateContent(
                errorMessage = uiState.error.asString(),
                onRetryClick = { onIntent(CheckoutUiIntent.RetryClicked) },
                errorType = AlertType.Error,
                modifier = Modifier.align(Alignment.Center)
            )

            uiState.isEmpty -> InfoStateContent(
                errorMessage = stringResource(R.string.checkout_no_addresses),
                caption = stringResource(R.string.checkout_add_address),
                onRetryClick = { onIntent(CheckoutUiIntent.AddAddressClicked) },
                errorType = AlertType.Warning,
                modifier = Modifier.align(Alignment.Center)
            )

            else -> LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Theme.space.large),
                verticalArrangement = Arrangement.spacedBy(Theme.space.large)
            ) {
                items(items = uiState.addresses, key = { it.id }) { address ->
                    AddressCard(
                        name = address.title,
                        address = address.fullAddress,
                        phone = address.phone,
                        isSelected = address.id == uiState.selectedAddressId,
                        onSelected = { onIntent(CheckoutUiIntent.AddressSelected(address.id)) },
                        onEditClick = { onIntent(CheckoutUiIntent.EditAddressClicked(address.id)) },
                        onDeleteClick = { onIntent(CheckoutUiIntent.DeleteAddressClicked(address.id)) }
                    )
                }
            }
        }

        uiState.pendingDelete?.let { address ->
            DeleteAddressDialog(
                address = address,
                isDeleting = uiState.isDeleting,
                onConfirm = { onIntent(CheckoutUiIntent.DeleteConfirmed) },
                onDismiss = { onIntent(CheckoutUiIntent.DeleteDismissed) }
            )
        }
    }
}

@Composable
private fun DeleteAddressDialog(
    address: ShippingAddressUi,
    isDeleting: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            LafyuuText(
                text = stringResource(R.string.checkout_delete_title),
                style = Theme.typography.heading5,
                color = Theme.color.neutralDark
            )
        },
        text = {
            LafyuuText(
                text = stringResource(R.string.checkout_delete_message, address.title),
                style = Theme.typography.normalTextRegular,
                color = Theme.color.neutralGrey
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm, enabled = !isDeleting) {
                LafyuuText(
                    text = stringResource(R.string.checkout_delete_confirm),
                    style = Theme.typography.normalCaptionBold,
                    color = Theme.color.red
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, enabled = !isDeleting) {
                LafyuuText(
                    text = stringResource(R.string.checkout_delete_cancel),
                    style = Theme.typography.normalCaptionBold,
                    color = Theme.color.neutralGrey
                )
            }
        }
    )
}
