package com.elhady.lafyuu.feature.auth.presentation.reset_password

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.elhady.lafyuu.core.designsystem.component.button.LafyuuButton
import com.elhady.lafyuu.core.designsystem.component.input.LafyuuPasswordField
import com.elhady.lafyuu.core.designsystem.component.input.LafyuuTextField
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ResetPasswordRoute(
    onNavigateToLogin: () -> Unit,
    viewModel: ResetPasswordViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                ResetPasswordUiEffect.NavigateToLogin -> onNavigateToLogin()
                is ResetPasswordUiEffect.ShowError -> snackbarHostState.showSnackbar(effect.message)
            }
        }
    }

    ResetPasswordScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        snackbarHostState = snackbarHostState
    )
}

@Composable
internal fun ResetPasswordScreen(
    uiState: ResetPasswordUiState,
    onEvent: (ResetPasswordUiEvent) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Reset Password",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Enter the code sent to your email and your new password",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            LafyuuTextField(
                value = uiState.otp,
                onValueChange = { onEvent(ResetPasswordUiEvent.OtpChanged(it)) },
                label = "OTP Code",
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            LafyuuPasswordField(
                value = uiState.newPassword,
                onValueChange = { onEvent(ResetPasswordUiEvent.NewPasswordChanged(it)) },
                label = "New Password",
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            LafyuuPasswordField(
                value = uiState.confirmPassword,
                onValueChange = { onEvent(ResetPasswordUiEvent.ConfirmPasswordChanged(it)) },
                label = "Confirm Password",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            LafyuuButton(
                text = "Reset Password",
                onClick = { onEvent(ResetPasswordUiEvent.ResetClicked) },
                loading = uiState.isLoading,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
fun ResetPasswordScreenPreview() {
    ResetPasswordScreen(
        uiState = ResetPasswordUiState(),
        onEvent = {},
        snackbarHostState = remember { SnackbarHostState() }
    )
}