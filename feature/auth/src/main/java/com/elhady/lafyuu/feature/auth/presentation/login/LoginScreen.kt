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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.elhady.lafyuu.core.designsystem.R
import com.elhady.lafyuu.core.designsystem.components.button.DefaultButton
import com.elhady.lafyuu.core.designsystem.components.button.SocialButton
import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.core.designsystem.components.element.LafyuuScaffold
import com.elhady.lafyuu.core.designsystem.components.element.OrDivider
import com.elhady.lafyuu.core.designsystem.components.element.VerticalSpacerExtraLarge
import com.elhady.lafyuu.core.designsystem.components.element.VerticalSpacerLarge
import com.elhady.lafyuu.core.designsystem.components.element.VerticalSpacerMedium
import com.elhady.lafyuu.core.designsystem.components.element.VerticalSpacerSmall
import com.elhady.lafyuu.core.designsystem.components.other.LafyuuSnackBarVisuals
import com.elhady.lafyuu.core.designsystem.components.text.LafyuuText
import com.elhady.lafyuu.core.designsystem.components.textfield.EmailTextField
import com.elhady.lafyuu.core.designsystem.components.textfield.PasswordTextField
import com.elhady.lafyuu.core.designsystem.icons.Google
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTheme
import com.elhady.lafyuu.core.designsystem.theme.Theme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoginScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                LoginUiEffect.NavigateToHome -> onNavigateToHome()
                LoginUiEffect.NavigateToRegister -> onNavigateToRegister()
                LoginUiEffect.NavigateToForgotPassword -> onNavigateToForgotPassword()
                is LoginUiEffect.ShowError -> snackbarHostState.showSnackbar(
                    visuals = LafyuuSnackBarVisuals(
                        message = effect.message,
                        type = AlertType.Error
                    )
                )

                is LoginUiEffect.ShowMessage -> snackbarHostState.showSnackbar(
                    visuals = LafyuuSnackBarVisuals(
                        message = effect.message,
                        type = AlertType.Success
                    )
                )
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
        snackbarHostState = snackbarHostState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = Theme.space.large)
                .verticalScroll(rememberScrollState())
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = "Logo",
            )
            VerticalSpacerLarge()
            LafyuuText(
                text = "Welcome to Lafyuu",
                style = Theme.typography.heading4,
                color = Theme.color.neutralDark,
            )
            VerticalSpacerSmall()
            LafyuuText(
                text = "Sign in to continue",
                style = Theme.typography.normalTextRegular,
                color = Theme.color.neutralGrey,
                modifier = Modifier.padding(bottom = Theme.space.huge)
            )
            EmailTextField(
                value = uiState.email,
                onValueChange = { onEvent(LoginUiEvent.EmailChanged(it)) },
                placeholder = "Email",
                errorMessage = uiState.emailError,
                modifier = Modifier.fillMaxWidth()
            )
            VerticalSpacerLarge()
            PasswordTextField(
                value = uiState.password,
                onValueChange = { onEvent(LoginUiEvent.PasswordChanged(it)) },
                placeholder = "Password",
                errorMessage = uiState.passwordError,
                modifier = Modifier.fillMaxWidth()
            )

            VerticalSpacerLarge()

            DefaultButton(
                caption = "Login",
                onClick = { onEvent(LoginUiEvent.LoginClicked) },
                isLoading = uiState.isLoading,
                modifier = Modifier.fillMaxWidth()
            )

            VerticalSpacerExtraLarge()

            OrDivider()

            SocialButton(
                icon = Google,
                caption = "Login with Google",
                onClick = { onEvent(LoginUiEvent.GoogleLoginClicked("idToken")) },
                isLoading = uiState.isLoading,
                modifier = Modifier.fillMaxWidth()
            )

            VerticalSpacerExtraLarge()

            LafyuuText(
                text = "Forgot Password?",
                style = Theme.typography.largeLinkBold,
                onClick = onNavigateToForgotPassword,
            )
            VerticalSpacerMedium()
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                LafyuuText(
                    text = "Don't have an account? ",
                    style = Theme.typography.mediumTextRegular,
                    color = Theme.color.neutralGrey,
                )
                LafyuuText(
                    text = "Register",
                    modifier = Modifier.clickable { onNavigateToRegister() },
                    style = Theme.typography.mediumTextBold,
                    onClick = onNavigateToRegister,
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
