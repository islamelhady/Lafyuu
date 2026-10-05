package com.elhady.lafyuu.feature.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.feature.profile.domain.usecase.ChangePasswordUseCase
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
    private val changePasswordUseCase: ChangePasswordUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChangePasswordUiState())
    val uiState: StateFlow<ChangePasswordUiState> = _uiState.asStateFlow()

    private val _uiEffect = Channel<ChangePasswordUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    fun onEvent(event: ChangePasswordUiEvent) {
        when (event) {
            is ChangePasswordUiEvent.CurrentPasswordChanged -> {
                _uiState.update { it.copy(currentPassword = event.value, error = null) }
            }
            is ChangePasswordUiEvent.NewPasswordChanged -> {
                _uiState.update { it.copy(newPassword = event.value, error = null) }
            }
            is ChangePasswordUiEvent.ConfirmNewPasswordChanged -> {
                _uiState.update { it.copy(confirmNewPassword = event.value, error = null) }
            }
            ChangePasswordUiEvent.SubmitClicked -> changePassword()
            ChangePasswordUiEvent.BackClicked -> sendEffect(ChangePasswordUiEffect.NavigateBack)
        }
    }

    private fun changePassword() {
        val currentPassword = _uiState.value.currentPassword
        val newPassword = _uiState.value.newPassword
        val confirmNewPassword = _uiState.value.confirmNewPassword

        if (currentPassword.isBlank() || newPassword.isBlank() || confirmNewPassword.isBlank()) {
            _uiState.update { it.copy(error = "Please fill all fields") }
            return
        }
        if (newPassword != confirmNewPassword) {
            _uiState.update { it.copy(error = "New passwords do not match") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = changePasswordUseCase(currentPassword, newPassword, confirmNewPassword)) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    sendEffect(ChangePasswordUiEffect.ShowSnackbar(message = "Password changed successfully", type = AlertType.Success))
                    sendEffect(ChangePasswordUiEffect.NavigateBack)
                }
                is AppResult.Error -> {
                    val msg = result.message ?: "Failed to change password"
                    _uiState.update { it.copy(isLoading = false, error = msg) }
                    sendEffect(ChangePasswordUiEffect.ShowSnackbar(message = msg, type = AlertType.Error))
                }
                is AppResult.Loading -> {}
            }
        }
    }

    private fun sendEffect(effect: ChangePasswordUiEffect) {
        viewModelScope.launch {
            _uiEffect.send(effect)
        }
    }
}
