package com.elhady.lafyuu.feature.auth.presentation.change_password

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.elhady.lafyuu.core.designsystem.components.button.DefaultButton
import com.elhady.lafyuu.core.designsystem.components.element.LafyuuScaffold
import com.elhady.lafyuu.core.designsystem.components.text.LafyuuText
import com.elhady.lafyuu.core.designsystem.components.textfield.PasswordTextField
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTheme
import com.elhady.lafyuu.core.designsystem.theme.Theme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ChangePasswordScreen(
    onNavigateBack: () -> Unit,
    viewModel: ChangePasswordViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                ChangePasswordUiEffect.NavigateBack -> onNavigateBack()
                is ChangePasswordUiEffect.ShowError -> snackbarHostState.showSnackbar(effect.message)
                is ChangePasswordUiEffect.ShowSuccessMessage -> snackbarHostState.showSnackbar(effect.message)
            }
        }
    }

    ChangePasswordContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        snackbarHostState = snackbarHostState
    )
}

@Composable
private fun ChangePasswordContent(
    uiState: ChangePasswordUiState,
    onEvent: (ChangePasswordUiEvent) -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    LafyuuScaffold(
        toast = { SnackbarHost(snackbarHostState) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Theme.space.extraLarge)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LafyuuText(
                text = "Change Password",
                style = Theme.typography.heading4,
                modifier = Modifier.padding(bottom = Theme.space.small)
            )

            Spacer(modifier = Modifier.height(Theme.space.extraLarge))

            PasswordTextField(
                value = uiState.currentPassword,
                onValueChange = { onEvent(ChangePasswordUiEvent.CurrentPasswordChanged(it)) },
                placeholder = "Old Password",
                errorMessage = uiState.currentPasswordError,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(Theme.space.large))

            PasswordTextField(
                value = uiState.newPassword,
                onValueChange = { onEvent(ChangePasswordUiEvent.NewPasswordChanged(it)) },
                placeholder = "New Password",
                errorMessage = uiState.newPasswordError,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(Theme.space.large))

            PasswordTextField(
                value = uiState.confirmPassword,
                onValueChange = { onEvent(ChangePasswordUiEvent.ConfirmPasswordChanged(it)) },
                placeholder = "New Password Again",
                errorMessage = uiState.confirmPasswordError,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(Theme.space.extraLarge))

            DefaultButton(
                caption = "Save",
                onClick = { onEvent(ChangePasswordUiEvent.SaveClicked) },
                isLoading = uiState.isLoading,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
private fun ChangePasswordContentPreview() {
    LafyuuTheme {
        ChangePasswordContent(
            uiState = ChangePasswordUiState(),
            onEvent = {}
        )
    }
}
