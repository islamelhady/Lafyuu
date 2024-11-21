package com.elhady.lafyuu.feature.address.presentation

import Left
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import com.elhady.lafyuu.core.designsystem.components.appbar.SingleTopAppBar
import com.elhady.lafyuu.core.designsystem.components.button.DefaultButton
import com.elhady.lafyuu.core.designsystem.components.card.AddressCard
import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.core.designsystem.components.element.InfoStateContent
import com.elhady.lafyuu.core.designsystem.components.element.LafyuuScaffold
import com.elhady.lafyuu.core.designsystem.components.other.LafyuuSnackBarVisuals
import com.elhady.lafyuu.core.designsystem.components.text.LafyuuText
import com.elhady.lafyuu.core.designsystem.theme.Theme
import com.elhady.lafyuu.core.ui.text.asString
import com.elhady.lafyuu.feature.address.R
import com.elhady.lafyuu.feature.address.presentation.model.AddressUi
import kotlinx.coroutines.launch

@Composable
fun AddressRoute(
    onNavigateBack: () -> Unit,
    onNavigateToAddAddress: () -> Unit,
    onNavigateToEditAddress: (String) -> Unit,
    viewModel: AddressViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // first open and every return from the add/edit screens
    LifecycleEventEffect(Lifecycle.Event.ON_START) {
        viewModel.onIntent(AddressUiIntent.Refresh)
    }

    LaunchedEffect(viewModel, lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.uiEffect.collect { effect ->
                when (effect) {
                    AddressUiEffect.NavigateBack -> onNavigateBack()
                    AddressUiEffect.NavigateToAddAddress -> onNavigateToAddAddress()
                    is AddressUiEffect.NavigateToEditAddress ->
                        onNavigateToEditAddress(effect.addressId)

                    is AddressUiEffect.ShowSnackbar -> {
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

    AddressScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onIntent = viewModel::onIntent
    )
}

@Composable
fun AddressScreen(
    uiState: AddressUiState,
    snackbarHostState: SnackbarHostState,
    onIntent: (AddressUiIntent) -> Unit
) {
    LafyuuScaffold(
        snackbarHostState = snackbarHostState,
        topBar = {
            SingleTopAppBar(
                title = stringResource(R.string.address_title),
                leadingIcon = Left,
                onLeadingClick = { onIntent(AddressUiIntent.BackClicked) }
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Theme.color.backgroundWhite)
                    .padding(Theme.space.large)
            ) {
                DefaultButton(
                    caption = stringResource(R.string.add_address),
                    onClick = { onIntent(AddressUiIntent.AddAddressClicked) },
                    modifier = Modifier.fillMaxWidth()
                )
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
                onRetryClick = { onIntent(AddressUiIntent.RetryClicked) },
                errorType = AlertType.Error,
                modifier = Modifier.align(Alignment.Center)
            )

            uiState.isEmpty -> InfoStateContent(
                errorMessage = stringResource(R.string.no_addresses_found_please_add_one),
                caption = stringResource(R.string.add_address),
                onRetryClick = { onIntent(AddressUiIntent.AddAddressClicked) },
                errorType = AlertType.Warning,
                modifier = Modifier.align(Alignment.Center)
            )

            else -> LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = Theme.space.large),
                contentPadding = PaddingValues(vertical = Theme.space.large),
                verticalArrangement = Arrangement.spacedBy(Theme.space.medium)
            ) {
                items(items = uiState.addresses, key = { it.id }) { address ->
                    AddressCard(
                        name = address.title,
                        address = address.fullAddress,
                        phone = address.phone,
                        isSelected = address.id == uiState.selectedAddressId,
                        onSelected = { onIntent(AddressUiIntent.AddressSelected(address.id)) },
                        onEditClick = { onIntent(AddressUiIntent.EditAddressClicked(address.id)) },
                        onDeleteClick = { onIntent(AddressUiIntent.DeleteAddressClicked(address.id)) }
                    )
                }
            }
        }

        uiState.pendingDelete?.let { address ->
            DeleteAddressDialog(
                address = address,
                isDeleting = uiState.isDeleting,
                onConfirm = { onIntent(AddressUiIntent.DeleteConfirmed) },
                onDismiss = { onIntent(AddressUiIntent.DeleteDismissed) }
            )
        }
    }
}

@Composable
private fun DeleteAddressDialog(
    address: AddressUi,
    isDeleting: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            LafyuuText(
                text = stringResource(R.string.address_delete_title),
                style = Theme.typography.heading5,
                color = Theme.color.neutralDark
            )
        },
        text = {
            LafyuuText(
                text = stringResource(R.string.address_delete_message, address.title),
                style = Theme.typography.normalTextRegular,
                color = Theme.color.neutralGrey
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm, enabled = !isDeleting) {
                LafyuuText(
                    text = stringResource(R.string.address_delete_confirm),
                    style = Theme.typography.normalCaptionBold,
                    color = Theme.color.red
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, enabled = !isDeleting) {
                LafyuuText(
                    text = stringResource(R.string.address_delete_cancel),
                    style = Theme.typography.normalCaptionBold,
                    color = Theme.color.neutralGrey
                )
            }
        }
    )
}