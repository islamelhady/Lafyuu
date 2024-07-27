package com.elhady.lafyuu.feature.checkout.presentation

import Left
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.elhady.lafyuu.core.designsystem.components.appbar.ProductTopBar
import com.elhady.lafyuu.core.designsystem.components.appbar.SingleTopAppBar
import com.elhady.lafyuu.core.designsystem.components.button.DefaultButton
import com.elhady.lafyuu.core.designsystem.components.card.AddressCard
import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.core.designsystem.components.element.LafyuuScaffold
import com.elhady.lafyuu.core.designsystem.components.other.LafyuuSnackBarVisuals
import com.elhady.lafyuu.core.designsystem.components.text.LafyuuText
import com.elhady.lafyuu.core.designsystem.icons.Plus
import com.elhady.lafyuu.core.designsystem.theme.Theme
import com.elhady.lafyuu.feature.checkout.domain.model.Address

@Composable
fun CheckoutRoute(
    onNavigateBack: () -> Unit,
    onNavigateToPayment: (String, String) -> Unit,
    onNavigateToAddAddress: () -> Unit,
    onNavigateToEditAddress: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CheckoutViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.loadAddresses()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                CheckoutUiEffect.NavigateBack -> onNavigateBack()
                is CheckoutUiEffect.NavigateToPayment -> onNavigateToPayment(
                    effect.shippingAddressId,
                    effect.paymentMethod
                )

                CheckoutUiEffect.NavigateToAddAddress -> onNavigateToAddAddress()
                is CheckoutUiEffect.NavigateToEditAddress -> onNavigateToEditAddress(effect.addressId)
                is CheckoutUiEffect.ShowSnackbar -> {
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

    CheckoutScreen(
        uiState = uiState,
        snackBarHostState = snackBarHostState,
        onEvent = viewModel::onEvent,
        modifier = modifier
    )
}

@Composable
fun CheckoutScreen(
    uiState: CheckoutUiState,
    snackBarHostState: SnackbarHostState,
    onEvent: (CheckoutUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    LafyuuScaffold(
        snackbarHostState = snackBarHostState,
        topBar = {
            ProductTopBar(
                title = "Ship To",
                leadingIcon = Left,
                onLeadingClick = { onEvent(CheckoutUiEvent.BackClicked) },
                trailingIcon = Plus,
                onTrailingClick = { onEvent(CheckoutUiEvent.OpenAddAddress) }
            )
        },
        bottomBar = {
            DefaultButton(
                caption = "Next",
                isLoading = false,
                onClick = { onEvent(CheckoutUiEvent.NextClicked) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Theme.space.large)
            )
        }
    ) {
        when {
            uiState.isLoading && uiState.addresses.isEmpty() -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Theme.color.blue
                )
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(Theme.space.large),
                    verticalArrangement = Arrangement.spacedBy(Theme.space.large)
                ) {
                    item {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(Theme.space.small)
                        ) {
                            if (uiState.addresses.isEmpty()) {
                                LafyuuText(
                                    text = "No addresses found. Please add one.",
                                    style = Theme.typography.normalTextRegular,
                                    color = Theme.color.neutralGrey
                                )
                            } else {
                                uiState.addresses.forEach { address ->
                                    val isSelected = uiState.selectedAddress?.id == address.id
                                    AddressCard(
                                        name = "${address.state}, ${address.city}",
                                        address = address.fullAddress,
                                        phone = "+2${address.phoneNumber}",
                                        isSelected = isSelected,
                                        onSelected = {
                                            onEvent(
                                                CheckoutUiEvent.SelectAddress(
                                                    address = address
                                                )
                                            )
                                        },
                                        onEditClick = {
                                            onEvent(
                                                CheckoutUiEvent.OpenEditAddress(addressId = address.id)
                                            )
                                        },
                                        onDeleteClick = {
                                            onEvent(
                                                CheckoutUiEvent.DeleteAddress(addressId = address.id)
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CheckoutSuccessScreen(
    message: String,
    onDismiss: () -> Unit
) {
    LafyuuScaffold(
        topBar = {
            SingleTopAppBar(
                title = "Success",
                onLeadingClick = {}
            )
        }
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
                    text = "Success!",
                    style = Theme.typography.heading2,
                    color = Theme.color.blue
                )
                LafyuuText(
                    text = message,
                    style = Theme.typography.normalTextRegular,
                    color = Theme.color.neutralGrey
                )
                DefaultButton(
                    caption = "Back to Home",
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
