package com.elhady.lafyuu.feature.auth.presentation.reset_password

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import com.elhady.lafyuu.core.designsystem.components.element.LafyuuScaffold
import com.elhady.lafyuu.core.designsystem.components.element.VerticalSpacerExtraLarge
import com.elhady.lafyuu.core.designsystem.components.element.VerticalSpacerLarge
import com.elhady.lafyuu.core.designsystem.components.element.VerticalSpacerSmall
import com.elhady.lafyuu.core.designsystem.components.text.LafyuuText
import com.elhady.lafyuu.core.designsystem.components.textfield.PasswordTextField
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTheme
import com.elhady.lafyuu.core.designsystem.theme.Theme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ResetPasswordScreen(
    onNavigateToLogin: () -> Unit,
    viewModel: ResetPasswordViewModel = hiltViewModel(),
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

    ResetPasswordContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        snackbarHostState = snackbarHostState
    )
}

@Composable
private fun ResetPasswordContent(
    uiState: ResetPasswordUiState,
    onEvent: (ResetPasswordUiEvent) -> Unit,
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
                text = "Reset Password?",
                style = Theme.typography.heading4,
                color = Theme.color.neutralDark,
            )
            VerticalSpacerSmall()
            LafyuuText(
                text = "Create a new password for your account",
                style = Theme.typography.normalTextRegular,
                color = Theme.color.neutralGrey,
                modifier = Modifier.padding(horizontal = Theme.space.large),
                textAlign = TextAlign.Center,
            )

            VerticalSpacerExtraLarge()

            PasswordTextField(
                value = uiState.newPassword,
                onValueChange = { onEvent(ResetPasswordUiEvent.NewPasswordChanged(it)) },
                placeholder = "New Password",
                modifier = Modifier.fillMaxWidth()
            )
            VerticalSpacerSmall()
            PasswordTextField(
                value = uiState.confirmPassword,
                onValueChange = { onEvent(ResetPasswordUiEvent.ConfirmPasswordChanged(it)) },
                placeholder = "Confirm Password",
                modifier = Modifier.fillMaxWidth()
            )

            VerticalSpacerExtraLarge()

            DefaultButton(
                caption = "Reset Password",
                onClick = { onEvent(ResetPasswordUiEvent.ResetClicked) },
                isLoading = uiState.isLoading,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
private fun ResetPasswordContentPreview() {
    LafyuuTheme {
        ResetPasswordContent(
            uiState = ResetPasswordUiState(),
            onEvent = {}
        )
    }
}
