package com.elhady.lafyuu.feature.auth.presentation.email_verification

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
import com.elhady.lafyuu.core.designsystem.components.card.InformationCard
import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.core.designsystem.components.element.LafyuuScaffold
import com.elhady.lafyuu.core.designsystem.components.element.VerticalSpacerExtraLarge
import com.elhady.lafyuu.core.designsystem.components.element.VerticalSpacerLarge
import com.elhady.lafyuu.core.designsystem.components.element.VerticalSpacerSmall
import com.elhady.lafyuu.core.designsystem.components.other.LafyuuSnackBarVisuals
import com.elhady.lafyuu.core.designsystem.components.text.LafyuuText
import com.elhady.lafyuu.core.designsystem.components.textfield.OtpCodeTextField
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTheme
import com.elhady.lafyuu.core.designsystem.theme.Theme
import kotlinx.coroutines.flow.collectLatest
import java.util.Locale

@Composable
fun EmailVerificationScreen(
    onNavigateToLogin: () -> Unit,
    viewModel: EmailVerificationViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                EmailVerificationUiEffect.NavigateToLogin -> onNavigateToLogin()
                is EmailVerificationUiEffect.ShowError -> snackBarHostState.showSnackbar(
                    visuals = LafyuuSnackBarVisuals(
                        message = effect.message,
                        type = AlertType.Error
                    )
                )

                is EmailVerificationUiEffect.ShowSuccessMessage -> snackBarHostState.showSnackbar(
                    visuals = LafyuuSnackBarVisuals(
                        message = effect.message,
                        type = AlertType.Success
                    )
                )
            }
        }
    }

    EmailVerificationContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onNavigateToLogin = onNavigateToLogin,
        snackBarHostState = snackBarHostState
    )
}

@Composable
private fun EmailVerificationContent(
    uiState: EmailVerificationUiState,
    onEvent: (EmailVerificationUiEvent) -> Unit,
    onNavigateToLogin: () -> Unit,
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    LafyuuScaffold(
        snackbarHostState = snackBarHostState
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
                text = "Verify your Email",
                style = Theme.typography.heading4,
                color = Theme.color.neutralDark,
            )
            VerticalSpacerSmall()
            LafyuuText(
                text = "We've sent a 6-digit verification code to your email address",
                style = Theme.typography.normalTextRegular,
                color = Theme.color.neutralGrey,
                textAlign = TextAlign.Center,
            )
            VerticalSpacerSmall()
            LafyuuText(
                text = uiState.email.ifEmpty { "example@gmail.com" },
                style = Theme.typography.normalTextBold,
                color = Theme.color.neutralDark,
                textAlign = TextAlign.Center,
            )
            VerticalSpacerExtraLarge()
            InformationCard()
            VerticalSpacerLarge()
            OtpCodeTextField(
                value = uiState.otp,
                onValueChange = { onEvent(EmailVerificationUiEvent.OtpChanged(it)) },
                isError = uiState.otpError != null,
                errorMessage = uiState.otpError,
            )
            VerticalSpacerLarge()

                LafyuuText(
                    text = "Didn't receive code? ",
                    color = Theme.color.neutralGrey,
                    style = Theme.typography.normalTextRegular
                )
                val resendText = if (uiState.isResendEnabled) {
                    "Resend Code"
                } else {
                    "Resend Code (${formatTimer(uiState.timerSeconds)})"
                }
                LafyuuText(
                    text = resendText,
                    style = Theme.typography.normalTextBold,
                    color = if (uiState.isResendEnabled) Theme.color.blue else Theme.color.neutralGrey,
                    onClick = if (uiState.isResendEnabled) {
                        { onEvent(EmailVerificationUiEvent.ResendOtpClicked) }
                    } else null
                )

            VerticalSpacerExtraLarge()
            DefaultButton(
                caption = "Verify",
                onClick = { onEvent(EmailVerificationUiEvent.VerifyClicked) },
                isLoading = uiState.isLoading,
                modifier = Modifier.fillMaxWidth()
            )
            VerticalSpacerExtraLarge()

            LafyuuText(
                text = "Back to Login",
                style = Theme.typography.normalTextBold,
                onClick = { onNavigateToLogin() }
            )
        }
    }
}

private fun formatTimer(totalSeconds: Int): String {
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
}

@Preview
@Composable
private fun EmailVerificationContentPreview() {
    LafyuuTheme {
        EmailVerificationContent(
            uiState = EmailVerificationUiState(
                email = "example@gmail.com",
                timerSeconds = 45,
                isResendEnabled = false
            ),
            onEvent = {},
            onNavigateToLogin = {}
        )
    }
}
