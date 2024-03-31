package com.elhady.lafyuu.feature.auth.presentation.change_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.auth.domain.model.ValidationResult
import com.elhady.lafyuu.feature.auth.domain.usecase.ChangePasswordUseCase
import com.elhady.lafyuu.feature.auth.domain.validator.ValidateConfirmPassword
import com.elhady.lafyuu.feature.auth.domain.validator.ValidatePassword
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
class ChangePasswordViewModel @Inject constructor(
    private val changePasswordUseCase: ChangePasswordUseCase,
    private val validatePassword: ValidatePassword,
    private val validateConfirmPassword: ValidateConfirmPassword
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChangePasswordUiState())
    val uiState: StateFlow<ChangePasswordUiState> = _uiState.asStateFlow()

    private val _uiEffect = Channel<ChangePasswordUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    fun onEvent(event: ChangePasswordUiEvent) {
        when (event) {
            is ChangePasswordUiEvent.CurrentPasswordChanged -> {
                _uiState.update { it.copy(currentPassword = event.password, currentPasswordError = null, generalError = null) }
            }
            is ChangePasswordUiEvent.NewPasswordChanged -> {
                _uiState.update { it.copy(newPassword = event.password, newPasswordError = null, generalError = null) }
            }
            is ChangePasswordUiEvent.ConfirmPasswordChanged -> {
                _uiState.update { it.copy(confirmPassword = event.password, confirmPasswordError = null, generalError = null) }
            }
            ChangePasswordUiEvent.SaveClicked -> save()
        }
    }

    private fun save() {
        if (_uiState.value.isLoading) return

        val state = _uiState.value
        val currentPasswordResult = validatePassword(state.currentPassword)
        val newPasswordResult = validatePassword(state.newPassword)
        val confirmPasswordResult = validateConfirmPassword(state.newPassword, state.confirmPassword)

        val hasError = listOf(currentPasswordResult, newPasswordResult, confirmPasswordResult).any { it is ValidationResult.Error }

        if (hasError) {
            _uiState.update {
                it.copy(
                    currentPasswordError = if (currentPasswordResult is ValidationResult.Error.PasswordRequired) "Current password is required" else null,
                    newPasswordError = if (newPasswordResult is ValidationResult.Error.PasswordRequired) "New password is required" else null,
                    confirmPasswordError = when (confirmPasswordResult) {
                        ValidationResult.Error.ConfirmPasswordRequired -> "Confirm password is required"
                        ValidationResult.Error.PasswordMismatch -> "Passwords do not match"
                        else -> null
                    }
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, generalError = null) }

            when (val result = changePasswordUseCase(state.currentPassword, state.newPassword, state.confirmPassword)) {
                is AppResult.Success<*> -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _uiEffect.send(ChangePasswordUiEffect.ShowSuccessMessage("Password changed successfully"))
                    _uiEffect.send(ChangePasswordUiEffect.NavigateBack)
                }
                is AppResult.Error -> {
                    val errorMessage = result.message ?: "Change password failed"
                    _uiState.update { it.copy(isLoading = false, generalError = errorMessage) }
                    _uiEffect.send(ChangePasswordUiEffect.ShowError(errorMessage))
                }
                AppResult.Loading -> {}
            }
        }
    }
}
