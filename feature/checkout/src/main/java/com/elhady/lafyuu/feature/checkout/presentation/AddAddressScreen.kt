package com.elhady.lafyuu.feature.checkout.presentation

import Left
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.elhady.lafyuu.core.designsystem.components.appbar.SingleTopAppBar
import com.elhady.lafyuu.core.designsystem.components.button.DefaultButton
import com.elhady.lafyuu.core.designsystem.components.element.LafyuuScaffold
import com.elhady.lafyuu.core.designsystem.components.other.LafyuuSnackBarVisuals
import com.elhady.lafyuu.core.designsystem.components.textfield.DefaultTextField
import com.elhady.lafyuu.core.designsystem.components.textfield.LafyuuFieldState
import com.elhady.lafyuu.core.designsystem.theme.Theme

@Composable
fun AddAddressRoute(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddAddressViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                AddAddressUiEffect.NavigateBack -> onNavigateBack()
                is AddAddressUiEffect.ShowSnackBar -> {
                    snackbarHostState.showSnackbar(
                        visuals = LafyuuSnackBarVisuals(
                            message = effect.message,
                            type = effect.type
                        )
                    )
                }
            }
        }
    }

    AddAddressScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onEvent = viewModel::onEvent,
        modifier = modifier
    )
}

@Composable
fun AddAddressScreen(
    uiState: AddAddressUiState,
    snackbarHostState: SnackbarHostState,
    onEvent: (AddAddressUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    LafyuuScaffold(
        snackbarHostState = snackbarHostState,
        topBar = {
            SingleTopAppBar(
                title = "Add Address",
                leadingIcon = Left,
                onLeadingClick = { onEvent(AddAddressUiEvent.BackClicked) }
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
                    isLoading = uiState.isLoading,
                    onClick = { onEvent(AddAddressUiEvent.AddAddressClicked) },
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(Theme.space.medium)
            ) {
                DefaultTextField(
                    value = uiState.state,
                    onValueChange = { onEvent(AddAddressUiEvent.FieldChanged(AddressField.STATE, it)) },
                    placeholder = "State",
                    state = if (uiState.stateError != null) LafyuuFieldState.Error else LafyuuFieldState.Default,
                    errorMessage = uiState.stateError
                )
                DefaultTextField(
                    value = uiState.city,
                    onValueChange = { onEvent(AddAddressUiEvent.FieldChanged(AddressField.CITY, it)) },
                    placeholder = "City",
                    state = if (uiState.cityError != null) LafyuuFieldState.Error else LafyuuFieldState.Default,
                    errorMessage = uiState.cityError
                )
                DefaultTextField(
                    value = uiState.street,
                    onValueChange = { onEvent(AddAddressUiEvent.FieldChanged(AddressField.STREET, it)) },
                    placeholder = "Street",
                    state = if (uiState.streetError != null) LafyuuFieldState.Error else LafyuuFieldState.Default,
                    errorMessage = uiState.streetError
                )
                DefaultTextField(
                    value = uiState.apartment,
                    onValueChange = { onEvent(AddAddressUiEvent.FieldChanged(AddressField.APARTMENT, it)) },
                    placeholder = "Apartment / Suite",
                    state = if (uiState.apartmentError != null) LafyuuFieldState.Error else LafyuuFieldState.Default,
                    errorMessage = uiState.apartmentError
                )
                DefaultTextField(
                    value = uiState.phoneNumber,
                    onValueChange = { onEvent(AddAddressUiEvent.FieldChanged(AddressField.PHONE, it)) },
                    placeholder = "Phone Number",
                    keyboardType = KeyboardType.Phone,
                    state = if (uiState.phoneError != null) LafyuuFieldState.Error else LafyuuFieldState.Default,
                    errorMessage = uiState.phoneError
                )
                DefaultTextField(
                    value = uiState.notes,
                    onValueChange = { onEvent(AddAddressUiEvent.FieldChanged(AddressField.NOTES, it)) },
                    placeholder = "Notes (Optional)"
                )
            }
        }
    }
}
