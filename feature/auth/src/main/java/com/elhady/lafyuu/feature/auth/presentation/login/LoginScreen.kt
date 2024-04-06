package com.elhady.lafyuu.feature.auth.presentation.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.elhady.lafyuu.core.designsystem.R
import com.elhady.lafyuu.core.designsystem.components.button.DefaultButton
import com.elhady.lafyuu.core.designsystem.components.element.LafyuuScaffold
import com.elhady.lafyuu.core.designsystem.components.element.OrDivider
import com.elhady.lafyuu.core.designsystem.components.text.LafyuuText
import com.elhady.lafyuu.core.designsystem.components.textfield.EmailTextField
import com.elhady.lafyuu.core.designsystem.components.textfield.PasswordTextField
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTheme
import com.elhady.lafyuu.core.designsystem.theme.Theme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoginScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    viewModel: LoginViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                LoginUiEffect.NavigateToHome -> onNavigateToHome()
                LoginUiEffect.NavigateToRegister -> onNavigateToRegister()
                LoginUiEffect.NavigateToForgotPassword -> onNavigateToForgotPassword()
                is LoginUiEffect.ShowError -> snackbarHostState.showSnackbar(effect.message)
                is LoginUiEffect.ShowMessage -> snackbarHostState.showSnackbar(effect.message)
            }
        }
    }

    LoginContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onNavigateToRegister = onNavigateToRegister,
        onNavigateToForgotPassword = onNavigateToForgotPassword,
        snackbarHostState = snackbarHostState
    )
}

@Composable
private fun LoginContent(
    uiState: LoginUiState,
    onEvent: (LoginUiEvent) -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier.padding(bottom = Theme.space.huge)
            )
            LafyuuText(
                text = "Welcome to Lafyuu",
                style = Theme.typography.heading4,
                modifier = Modifier.padding(bottom = Theme.space.small)
            )
            LafyuuText(
                text = "Sign in to continue",
                style = Theme.typography.normalTextRegular,
                modifier = Modifier.padding(bottom = Theme.space.huge)
            )

            EmailTextField(
                value = uiState.email,
                onValueChange = { onEvent(LoginUiEvent.EmailChanged(it)) },
                placeholder = "Email",
                errorMessage = uiState.emailError,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(Theme.space.large))
            PasswordTextField(
                value = uiState.password,
                onValueChange = { onEvent(LoginUiEvent.PasswordChanged(it)) },
                placeholder = "Password",
                errorMessage = uiState.passwordError,
                modifier = Modifier.fillMaxWidth()
            )

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                LafyuuText(
                    text = "Forgot Password?",
                    modifier = Modifier
                        .padding(top = Theme.space.large)
                        .clickable { onNavigateToForgotPassword() }
                )
            }

            Spacer(modifier = Modifier.height(Theme.space.extraLarge))

            DefaultButton(
                caption = "Login",
                onClick = { onEvent(LoginUiEvent.LoginClicked) },
                isLoading = uiState.isLoading,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(Theme.space.large))

            OrDivider()

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                LafyuuText(
                    text = "Don't have an account? "
                )
                LafyuuText(
                    text = "Register",
                    modifier = Modifier.clickable { onNavigateToRegister() },
                    style = Theme.typography.normalTextBold.copy(
                        textDecoration = TextDecoration.Underline
                    )
                )
            }
        }
    }
}

@Preview
@Composable
private fun LoginContentPreview() {
    LafyuuTheme {
        LoginContent(
            uiState = LoginUiState(),
            onEvent = {},
            onNavigateToRegister = {},
            onNavigateToForgotPassword = {}
        )
    }
}
