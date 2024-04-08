package com.elhady.lafyuu.feature.auth.presentation.forgot_password

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.elhady.lafyuu.core.designsystem.R
import com.elhady.lafyuu.core.designsystem.components.button.DefaultButton
import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.core.designsystem.components.element.LafyuuScaffold
import com.elhady.lafyuu.core.designsystem.components.element.VerticalSpacerExtraLarge
import com.elhady.lafyuu.core.designsystem.components.element.VerticalSpacerLarge
import com.elhady.lafyuu.core.designsystem.components.element.VerticalSpacerSmall
import com.elhady.lafyuu.core.designsystem.components.other.LafyuuSnackBarVisuals
import com.elhady.lafyuu.core.designsystem.components.text.LafyuuText
import com.elhady.lafyuu.core.designsystem.components.textfield.EmailTextField
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTheme
import com.elhady.lafyuu.core.designsystem.theme.Theme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ForgotPasswordScreen(
    onNavigateBack: () -> Unit,
    onNavigateToResetPassword: (String) -> Unit,
    viewModel: ForgotPasswordViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                is ForgotPasswordUiEffect.NavigateToResetPassword -> onNavigateToResetPassword(
                    effect.email
                )

                is ForgotPasswordUiEffect.ShowError -> snackbarHostState.showSnackbar(
                    visuals = LafyuuSnackBarVisuals(
                        message = effect.message,
                        type = AlertType.Error
                    )
                )
            }
        }
    }

    ForgotPasswordContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onNavigateBack = onNavigateBack,
        snackbarHostState = snackbarHostState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ForgotPasswordContent(
    uiState: ForgotPasswordUiState,
    onEvent: (ForgotPasswordUiEvent) -> Unit,
    onNavigateBack: () -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    LafyuuScaffold(
        snackbarHostState = snackbarHostState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = Theme.space.large)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = "Logo",
            )
            VerticalSpacerLarge()
            LafyuuText(
                text = "Forgot Password?",
                style = Theme.typography.heading4,
                color = Theme.color.neutralDark,
            )
            VerticalSpacerSmall()
            LafyuuText(
                text = "don't worry! Enter your email address and we'll send you a link to reset your password",
                style = Theme.typography.normalTextRegular,
                color = Theme.color.neutralGrey,
                modifier = Modifier.padding(horizontal = Theme.space.large),
                textAlign = TextAlign.Center,
            )

            VerticalSpacerExtraLarge()

            EmailTextField(
                value = uiState.email,
                onValueChange = { onEvent(ForgotPasswordUiEvent.EmailChanged(it)) },
                placeholder = "Email",
                modifier = Modifier.fillMaxWidth()
            )

            VerticalSpacerLarge()

            DefaultButton(
                caption = "Send Code",
                onClick = { onEvent(ForgotPasswordUiEvent.SubmitClicked) },
                isLoading = uiState.isLoading,
                modifier = Modifier.fillMaxWidth()
            )

            VerticalSpacerExtraLarge()

            LafyuuText(
                text = "Back to Login",
                style = Theme.typography.largeLinkBold,
                onClick = onNavigateBack,
            )
        }
    }
}

@Preview
@Composable
private fun ForgotPasswordContentPreview() {
    LafyuuTheme {
        ForgotPasswordContent(
            uiState = ForgotPasswordUiState(),
            onEvent = {},
            onNavigateBack = {}
        )
    }
}
