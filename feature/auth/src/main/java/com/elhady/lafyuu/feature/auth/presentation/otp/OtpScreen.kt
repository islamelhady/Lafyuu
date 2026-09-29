package com.elhady.lafyuu.feature.auth.presentation.otp

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

@Composable
fun OtpScreen(
    onNavigateToResetPassword: (String, String) -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: OtpViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                is OtpUiEffect.NavigateToResetPassword -> onNavigateToResetPassword(effect.email, effect.otp)
                OtpUiEffect.NavigateToLogin -> onNavigateToLogin()
                is OtpUiEffect.ShowError -> snackBarHostState.showSnackbar(
                    visuals = LafyuuSnackBarVisuals(
                        message = effect.message,
                        type = AlertType.Error
                    )
                )

                is OtpUiEffect.ShowMessage -> snackBarHostState.showSnackbar(
                    visuals = LafyuuSnackBarVisuals(
                        message = effect.message,
                        type = AlertType.Success
                    )
                )
            }
        }
    }

    OtpContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onNavigateToLogin = onNavigateToLogin,
        snackBarHostState = snackBarHostState
    )
}

@Composable
private fun OtpContent(
    uiState: OtpUiState,
    onEvent: (OtpUiEvent) -> Unit,
    onNavigateToLogin: () -> Unit = {},
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
                text = "Enter Verification Code",
                style = Theme.typography.heading4,
                color = Theme.color.neutralDark,
            )
            VerticalSpacerSmall()
            LafyuuText(
                text = "We've send a 6-digit verification code to your email address",
                style = Theme.typography.normalTextRegular,
                color = Theme.color.neutralGrey,
                textAlign = TextAlign.Center,
            )

            LafyuuText(
                text = uiState.email,
                style = Theme.typography.normalTextBold,
                color = Theme.color.neutralGrey,
                textAlign = TextAlign.Center,
            )

            VerticalSpacerExtraLarge()

            OtpCodeTextField(
                value = uiState.otp,
                onValueChange = { onEvent(OtpUiEvent.OtpChanged(it)) },
                isError = uiState.otpError != null,
                errorMessage = uiState.otpError,
            )
            VerticalSpacerLarge()
            LafyuuText(
                text = "Didn't receive code?  ",
                color = Theme.color.neutralGrey,
                style = Theme.typography.normalTextRegular
            )
            LafyuuText(
                text = "Resend Code",
                style = Theme.typography.normalTextBold,
                onClick = { onEvent(OtpUiEvent.ResendOtpClicked) }
            )
            VerticalSpacerExtraLarge()
            DefaultButton(
                caption = "Verify",
                onClick = { onEvent(OtpUiEvent.VerifyClicked) },
                isLoading = uiState.isLoading,
                modifier = Modifier.fillMaxWidth()
            )

            VerticalSpacerExtraLarge()

            LafyuuText(
                text = "Back to Login",
                style = Theme.typography.normalTextBold,
                onClick = onNavigateToLogin
            )
        }
    }
}

@Preview
@Composable
private fun OtpContentPreview() {
    LafyuuTheme {
        OtpContent(
            uiState = OtpUiState(email = "islam.elhadyy@gmail.com"),
            onEvent = {}
        )
    }
}
