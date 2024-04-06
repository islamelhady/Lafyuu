package com.elhady.lafyuu.feature.auth.presentation.otp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.elhady.lafyuu.core.designsystem.components.button.DefaultButton
import com.elhady.lafyuu.core.designsystem.components.text.LafyuuText
import com.elhady.lafyuu.core.designsystem.components.textfield.PhoneNumberTextField
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun OtpScreen(
    onNavigateToLogin: () -> Unit,
    viewModel: OtpViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                OtpUiEffect.NavigateToLogin -> onNavigateToLogin()
                is OtpUiEffect.ShowError -> snackBarHostState.showSnackbar(effect.message)
                is OtpUiEffect.ShowMessage -> snackBarHostState.showSnackbar(effect.message)
            }
        }
    }

    OtpContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        snackBarHostState = snackBarHostState
    )
}

@Composable
private fun OtpContent(
    uiState: OtpUiState,
    onEvent: (OtpUiEvent) -> Unit,
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) }
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
                text = "Verify Email",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "We sent a code to ${uiState.email}",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            PhoneNumberTextField(
                value = uiState.otp,
                onValueChange = { onEvent(OtpUiEvent.OtpChanged(it)) },
                modifier = Modifier.padding(bottom = 24.dp)
            )

            DefaultButton(
                caption = "Verify",
                onClick = { onEvent(OtpUiEvent.VerifyClicked) },
                isLoading = uiState.isLoading,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Didn't receive code?")
                LafyuuText(
                    text = "Resend",
                    onClick = { onEvent(OtpUiEvent.ResendOtpClicked) }
                )
            }
        }
    }
}

@Preview
@Composable
private fun OtpContentPreview() {
    LafyuuTheme {
        OtpContent(
            uiState = OtpUiState(email = "test@example.com"),
            onEvent = {}
        )
    }
}
