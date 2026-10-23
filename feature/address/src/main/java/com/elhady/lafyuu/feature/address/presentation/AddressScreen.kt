package com.elhady.lafyuu.feature.address.presentation

import Left
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.elhady.lafyuu.core.designsystem.components.appbar.SingleTopAppBar
import com.elhady.lafyuu.core.designsystem.components.button.DefaultButton
import com.elhady.lafyuu.core.designsystem.components.card.AddressCard
import com.elhady.lafyuu.core.designsystem.components.element.InfoStateContent
import com.elhady.lafyuu.core.designsystem.components.element.LafyuuScaffold
import com.elhady.lafyuu.core.designsystem.components.other.LafyuuSnackBarVisuals
import com.elhady.lafyuu.core.designsystem.theme.Theme
import com.elhady.lafyuu.feature.address.R

@Composable
fun AddressRoute(
    onNavigateBack: () -> Unit,
    onNavigateToAddAddress: () -> Unit,
    onNavigateToEditAddress: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddressViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.loadAddresses()
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                AddressUiEffect.NavigateBack -> onNavigateBack()
                AddressUiEffect.NavigateToAddAddress -> onNavigateToAddAddress()
                is AddressUiEffect.NavigateToEditAddress -> onNavigateToEditAddress(effect.addressId)
                is AddressUiEffect.ShowSnackbar -> {
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

    AddressScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onEvent = viewModel::onEvent,
        modifier = modifier
    )
}

@Composable
fun AddressScreen(
    uiState: AddressUiState,
    snackbarHostState: SnackbarHostState,
    onEvent: (AddressUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    LafyuuScaffold(
        snackbarHostState = snackbarHostState,
        topBar = {
            SingleTopAppBar(
                title = "Address",
                leadingIcon = Left,
                onLeadingClick = { onEvent(AddressUiEvent.BackClicked) }
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
                    caption = "Add Address",
                    onClick = { onEvent(AddressUiEvent.AddAddressClicked) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(Theme.space.large)
        ) {
            when {
                uiState.isLoading && uiState.addresses.isEmpty() -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Theme.color.blue
                    )
                }

                uiState.addresses.isEmpty() -> {
                    InfoStateContent(
                        errorMessage = stringResource(id = R.string.no_addresses_found_please_add_one),
                        onRetryClick = { onEvent(AddressUiEvent.LoadAddresses) },
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = Theme.space.large),
                        verticalArrangement = Arrangement.spacedBy(Theme.space.medium)
                    ) {
                        items(
                            items = uiState.addresses,
                            key = { it.id }
                        ) { address ->
                            AddressCard(
                                name = "${address.city}, ${address.state}",
                                address = address.fullAddress,
                                phone = "+2 ${address.phoneNumber}",
                                isSelected = address.id == uiState.selectedAddressId,
                                onSelected = { onEvent(AddressUiEvent.AddressSelected(address.id)) },
                                onEditClick = { onEvent(AddressUiEvent.EditAddressClicked(address.id)) },
                                onDeleteClick = { onEvent(AddressUiEvent.DeleteAddressClicked(address.id)) }
                            )
                        }
                    }
                }
            }
        }
    }
}
