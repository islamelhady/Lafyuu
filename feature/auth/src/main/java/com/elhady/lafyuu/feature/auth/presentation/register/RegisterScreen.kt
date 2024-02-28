package com.elhady.lafyuu.feature.auth.presentation.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.elhady.lafyuu.core.designsystem.component.button.LafyuuTextButton
import com.elhady.lafyuu.core.designsystem.component.input.LafyuuPasswordField
import com.elhady.lafyuu.core.designsystem.component.input.LafyuuTextField
import kotlinx.coroutines.flow.collectLatest

@Composable
fun RegisterRoute(
    onNavigateToLogin: () -> Unit,
    onNavigateToOtp: (String) -> Unit,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                RegisterUiEffect.NavigateToLogin -> onNavigateToLogin()
                RegisterUiEffect.NavigateToOtp -> onNavigateToOtp(uiState.email)
                is RegisterUiEffect.ShowError -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
            }
        }
    }

    RegisterScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onNavigateToLogin = onNavigateToLogin,
        snackbarHostState = snackbarHostState
    )
}

@Composable
internal fun RegisterScreen(
    uiState: RegisterUiState,
    onEvent: (RegisterUiEvent) -> Unit,
    onNavigateToLogin: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Create Account",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Join us to start shopping",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            Row(modifier = Modifier.fillMaxWidth()) {
                LafyuuTextField(
                    value = uiState.firstName,
                    onValueChange = { onEvent(RegisterUiEvent.FirstNameChanged(it)) },
                    label = "First Name",
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(16.dp))
                LafyuuTextField(
                    value = uiState.lastName,
                    onValueChange = { onEvent(RegisterUiEvent.LastNameChanged(it)) },
                    label = "Last Name",
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            LafyuuTextField(
                value = uiState.email,
                onValueChange = { onEvent(RegisterUiEvent.EmailChanged(it)) },
                label = "Email",
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            LafyuuPasswordField(
                value = uiState.password,
                onValueChange = { onEvent(RegisterUiEvent.PasswordChanged(it)) },
                label = "Password",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            LafyuuButton(
                text = "Register",
                onClick = { onEvent(RegisterUiEvent.RegisterClicked) },
                loading = uiState.isLoading,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Already have an account?")
                LafyuuTextButton(
                    text = "Login",
                    onClick = onNavigateToLogin
                )
            }
        }
    }
}

@Preview
@Composable
fun RegisterScreenPreview(){
    RegisterScreen(
        uiState = RegisterUiState(),
        onEvent = {},
        onNavigateToLogin = {},
        snackbarHostState = remember { SnackbarHostState() }
    )
}
