package com.elhady.lafyuu.feature.profile.presentation

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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.elhady.lafyuu.core.designsystem.components.appbar.SingleTopAppBar
import com.elhady.lafyuu.core.designsystem.components.button.DefaultButton
import com.elhady.lafyuu.core.designsystem.components.element.LafyuuScaffold
import com.elhady.lafyuu.core.designsystem.components.other.LafyuuSnackBarVisuals
import com.elhady.lafyuu.core.designsystem.components.text.LafyuuText
import com.elhady.lafyuu.core.designsystem.components.textfield.LafyuuFieldState
import com.elhady.lafyuu.core.designsystem.components.textfield.PasswordTextField
import com.elhady.lafyuu.core.designsystem.theme.Theme

@Composable
fun ChangePasswordRoute(
    onNavigateBack: () -> Unit,
    viewModel: ChangePasswordViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                ChangePasswordUiEffect.NavigateBack -> onNavigateBack()
                is ChangePasswordUiEffect.ShowSnackbar -> {
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

    ChangePasswordScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onEvent = viewModel::onEvent,
    )
}

@Composable
fun ChangePasswordScreen(
    uiState: ChangePasswordUiState,
    snackbarHostState: SnackbarHostState,
    onEvent: (ChangePasswordUiEvent) -> Unit,
) {
    LafyuuScaffold(
        snackbarHostState = snackbarHostState,
        topBar = {
            SingleTopAppBar(
                title = "Change Password",
                leadingIcon = Left,
                onLeadingClick = { onEvent(ChangePasswordUiEvent.BackClicked) }
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
                    caption = "Save",
                    isLoading = uiState.isLoading,
                    onClick = { onEvent(ChangePasswordUiEvent.SubmitClicked) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Theme.space.large)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(Theme.space.medium)
        ) {
            LafyuuText(
                text = "Old Password",
                style = Theme.typography.heading5,
                color = Theme.color.neutralDark,
            )
            PasswordTextField(
                value = uiState.currentPassword,
                onValueChange = { onEvent(ChangePasswordUiEvent.CurrentPasswordChanged(it)) },
                placeholder = "Old Password",
                state = if (!uiState.error.isNullOrBlank()) LafyuuFieldState.Error else LafyuuFieldState.Default,
                errorMessage = uiState.error,
                modifier = Modifier.padding(bottom = Theme.space.extraLarge)
            )
            LafyuuText(
                text = "New Password",
                style = Theme.typography.heading5,
                color = Theme.color.neutralDark,
            )
            PasswordTextField(
                value = uiState.newPassword,
                onValueChange = { onEvent(ChangePasswordUiEvent.NewPasswordChanged(it)) },
                placeholder = "New Password",
                state = if (!uiState.error.isNullOrBlank()) LafyuuFieldState.Error else LafyuuFieldState.Default,
                errorMessage = uiState.error,
                modifier = Modifier.padding(bottom = Theme.space.extraLarge)
            )
            LafyuuText(
                text = "New Password Again",
                style = Theme.typography.heading5,
                color = Theme.color.neutralDark,
            )
            PasswordTextField(
                value = uiState.confirmNewPassword,
                onValueChange = { onEvent(ChangePasswordUiEvent.ConfirmNewPasswordChanged(it)) },
                placeholder = "New Password Again",
                state = if (!uiState.error.isNullOrBlank()) LafyuuFieldState.Error else LafyuuFieldState.Default,
                errorMessage = uiState.error
            )
        }
    }
}
