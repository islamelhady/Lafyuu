package com.elhady.lafyuu.feature.auth.presentation.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.elhady.lafyuu.core.designsystem.R
import com.elhady.lafyuu.core.designsystem.components.button.DefaultButton
import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.core.designsystem.components.element.LafyuuScaffold
import com.elhady.lafyuu.core.designsystem.components.element.VerticalSpacerExtraLarge
import com.elhady.lafyuu.core.designsystem.components.element.VerticalSpacerLarge
import com.elhady.lafyuu.core.designsystem.components.element.VerticalSpacerSmall
import com.elhady.lafyuu.core.designsystem.components.other.LafyuuSnackBarVisuals
import com.elhady.lafyuu.core.designsystem.components.text.LafyuuText
import com.elhady.lafyuu.core.designsystem.components.textfield.DefaultTextField
import com.elhady.lafyuu.core.designsystem.components.textfield.EmailTextField
import com.elhady.lafyuu.core.designsystem.components.textfield.PasswordTextField
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTheme
import com.elhady.lafyuu.core.designsystem.theme.Theme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun RegisterScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToEmailVerification: (String) -> Unit,
    viewModel: RegisterViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                RegisterUiEffect.NavigateToLogin -> onNavigateToLogin()
                is RegisterUiEffect.NavigateToEmailVerification -> onNavigateToEmailVerification(effect.email)
                is RegisterUiEffect.ShowError -> snackbarHostState.showSnackbar(
                    visuals = LafyuuSnackBarVisuals(
                        message = effect.message,
                        type = AlertType.Error
                    )
                )
            }
        }
    }

    RegisterContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onNavigateToLogin = onNavigateToLogin,
        snackbarHostState = snackbarHostState
    )
}

@Composable
private fun RegisterContent(
    uiState: RegisterUiState,
    onEvent: (RegisterUiEvent) -> Unit,
    onNavigateToLogin: () -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    LafyuuScaffold(
        snackbarHostState = snackbarHostState,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Theme.space.extraLarge)
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
                text = "Let’s Get Started",
                style = Theme.typography.heading4,
                color = Theme.color.neutralDark,
            )
            VerticalSpacerSmall()
            LafyuuText(
                text = "Create a new account",
                style = Theme.typography.normalTextRegular,
                color = Theme.color.neutralGrey,
                modifier = Modifier.padding(horizontal = Theme.space.large),
                textAlign = TextAlign.Center,
            )

            VerticalSpacerExtraLarge()

            Row(modifier = Modifier.fillMaxWidth()) {
                DefaultTextField(
                    value = uiState.firstName,
                    onValueChange = { onEvent(RegisterUiEvent.FirstNameChanged(it)) },
                    placeholder = "First Name",
                    errorMessage = uiState.firstNameError,
                    modifier = Modifier.weight(1f)
                )
                VerticalSpacerLarge()
                DefaultTextField(
                    value = uiState.lastName,
                    onValueChange = { onEvent(RegisterUiEvent.LastNameChanged(it)) },
                    placeholder = "Last Name",
                    errorMessage = uiState.lastNameError,
                    modifier = Modifier.weight(1f)
                )
            }
            VerticalSpacerLarge()
            EmailTextField(
                value = uiState.email,
                onValueChange = { onEvent(RegisterUiEvent.EmailChanged(it)) },
                placeholder = "Email",
                errorMessage = uiState.emailError,
                modifier = Modifier.fillMaxWidth()
            )
            VerticalSpacerLarge()
            PasswordTextField(
                value = uiState.password,
                onValueChange = { onEvent(RegisterUiEvent.PasswordChanged(it)) },
                placeholder = "Password",
                errorMessage = uiState.passwordError,
                modifier = Modifier.fillMaxWidth()
            )
            VerticalSpacerLarge()
            PasswordTextField(
                value = uiState.confirmPassword,
                onValueChange = { onEvent(RegisterUiEvent.ConfirmPasswordChanged(it)) },
                placeholder = "Password Again",
                errorMessage = uiState.confirmPasswordError,
                modifier = Modifier.fillMaxWidth()
            )

            VerticalSpacerExtraLarge()

            DefaultButton(
                caption = "Sign Up",
                onClick = { onEvent(RegisterUiEvent.RegisterClicked) },
                isLoading = uiState.isLoading,
                modifier = Modifier.fillMaxWidth()
            )

            VerticalSpacerExtraLarge()

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                LafyuuText(
                    text = "have an account? "
                )
                LafyuuText(
                    text = "Sign In",
                    onClick = onNavigateToLogin,
                    style = Theme.typography.normalTextBold,
                )
            }
        }
    }
}

@Preview
@Composable
private fun RegisterContentPreview() {
    LafyuuTheme {
        RegisterContent(
            uiState = RegisterUiState(),
            onEvent = {},
            onNavigateToLogin = {}
        )
    }
}
