package com.elhady.lafyuu.feature.address.presentation

import Left
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.elhady.lafyuu.core.designsystem.components.appbar.SingleTopAppBar
import com.elhady.lafyuu.core.designsystem.components.button.DefaultButton
import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.core.designsystem.components.element.InfoStateContent
import com.elhady.lafyuu.core.designsystem.components.element.LafyuuScaffold
import com.elhady.lafyuu.core.designsystem.components.other.LafyuuSnackBarVisuals
import com.elhady.lafyuu.core.designsystem.components.textfield.DefaultTextField
import com.elhady.lafyuu.core.designsystem.components.textfield.LafyuuFieldState
import com.elhady.lafyuu.core.designsystem.theme.Theme
import com.elhady.lafyuu.core.ui.text.asString
import com.elhady.lafyuu.feature.address.R
import com.elhady.lafyuu.feature.address.domain.model.AddressField
import kotlinx.coroutines.launch

@Composable
fun AddressFormRoute(
    onNavigateBack: () -> Unit,
    viewModel: AddressFormViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(viewModel, lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.uiEffect.collect { effect ->
                when (effect) {
                    AddressFormUiEffect.NavigateBack -> onNavigateBack()

                    is AddressFormUiEffect.ShowSnackbar -> {
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

    AddressFormScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onIntent = viewModel::onIntent
    )
}

@Composable
fun AddressFormScreen(
    uiState: AddressFormUiState,
    snackbarHostState: SnackbarHostState,
    onIntent: (AddressFormUiIntent) -> Unit
) {
    LafyuuScaffold(
        snackbarHostState = snackbarHostState,
        topBar = {
            SingleTopAppBar(
                title = stringResource(
                    if (uiState.isEditMode) R.string.edit_address else R.string.add_address
                ),
                leadingIcon = Left,
                onLeadingClick = { onIntent(AddressFormUiIntent.BackClicked) }
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
                    caption = stringResource(
                        if (uiState.isEditMode) R.string.update_address else R.string.add_address
                    ),
                    isLoading = uiState.isSaving,
                    onClick = { onIntent(AddressFormUiIntent.SaveClicked) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    ) {
        when {
            uiState.isFetching -> CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Theme.color.blue
            )

            uiState.loadError != null -> InfoStateContent(
                errorMessage = uiState.loadError.asString(),
                onRetryClick = { onIntent(AddressFormUiIntent.RetryLoadClicked) },
                errorType = AlertType.Error,
                modifier = Modifier.align(Alignment.Center)
            )

            else -> Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(Theme.space.large),
                verticalArrangement = Arrangement.spacedBy(Theme.space.medium)
            ) {
                FormField(AddressField.STATE, R.string.address_hint_state, uiState, onIntent)
                FormField(AddressField.CITY, R.string.address_hint_city, uiState, onIntent)
                FormField(AddressField.STREET, R.string.address_hint_street, uiState, onIntent)
                FormField(AddressField.APARTMENT, R.string.address_hint_apartment, uiState, onIntent)
                FormField(
                    field = AddressField.PHONE,
                    placeholder = R.string.address_hint_phone,
                    uiState = uiState,
                    onIntent = onIntent,
                    keyboardType = KeyboardType.Phone
                )
                FormField(AddressField.NOTES, R.string.address_hint_notes, uiState, onIntent)
            }
        }
    }
}

@Composable
private fun FormField(
    field: AddressField,
    @StringRes placeholder: Int,
    uiState: AddressFormUiState,
    onIntent: (AddressFormUiIntent) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    val error = uiState.errors[field]
    DefaultTextField(
        value = uiState.form.valueOf(field),
        onValueChange = { onIntent(AddressFormUiIntent.FieldChanged(field, it)) },
        placeholder = stringResource(placeholder),
        keyboardType = keyboardType,
        state = if (error != null) LafyuuFieldState.Error else LafyuuFieldState.Default,
        errorMessage = error?.asString()
    )
}