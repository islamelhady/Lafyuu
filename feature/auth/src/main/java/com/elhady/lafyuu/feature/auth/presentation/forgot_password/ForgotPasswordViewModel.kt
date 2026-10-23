package com.elhady.lafyuu.feature.auth.presentation.forgot_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.ui.text.UiText
import com.elhady.lafyuu.core.ui.text.toUiText
import com.elhady.lafyuu.feature.auth.R
import com.elhady.lafyuu.feature.auth.domain.model.ValidationResult
import com.elhady.lafyuu.feature.auth.domain.usecase.ForgotPasswordUseCase
import com.elhady.lafyuu.feature.auth.domain.validator.ValidateEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val forgotPasswordUseCase: ForgotPasswordUseCase,
    private val validateEmail: ValidateEmail
) : ViewModel() {

    private val _uiState = MutableStateFlow(ForgotPasswordUiState())
    val uiState: StateFlow<ForgotPasswordUiState> = _uiState.asStateFlow()

    private val _uiEffect = Channel<ForgotPasswordUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    fun onEvent(event: ForgotPasswordUiEvent) {
        when (event) {
            is ForgotPasswordUiEvent.EmailChanged -> {
                _uiState.update { it.copy(email = event.email, emailError = null, error = null) }
            }
            ForgotPasswordUiEvent.SubmitClicked -> submit()
        }
    }

    private fun submit() {
        if (_uiState.value.isLoading) return

        val email = _uiState.value.email.trim()
        val emailResult = validateEmail(email)

        if (emailResult is ValidationResult.Error) {
            _uiState.update {
                it.copy(
                    emailError = when (emailResult) {
                        ValidationResult.Error.EmailRequired -> UiText.Resource(R.string.email_is_required)
                        ValidationResult.Error.InvalidEmailFormat -> UiText.Resource(R.string.invalid_email_format)
                        else -> null
                    }
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            when (val result = forgotPasswordUseCase(email)) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _uiEffect.send(ForgotPasswordUiEffect.NavigateToOtp(email))
                }
                is AppResult.Failure -> {
                    val message = result.error.toUiText()
                    _uiState.update { it.copy(isLoading = false, error = message) }
                    _uiEffect.send(ForgotPasswordUiEffect.ShowError(message))
                }
            }
        }
    }
}
