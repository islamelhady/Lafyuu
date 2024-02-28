package com.elhady.lafyuu.feature.auth.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.auth.domain.repository.AuthRepository
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
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    private val _uiEffect = Channel<RegisterUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    fun onEvent(event: RegisterUiEvent) {
        when (event) {
            is RegisterUiEvent.FirstNameChanged -> {
                _uiState.update { it.copy(firstName = event.name, error = null) }
            }
            is RegisterUiEvent.LastNameChanged -> {
                _uiState.update { it.copy(lastName = event.name, error = null) }
            }
            is RegisterUiEvent.EmailChanged -> {
                _uiState.update { it.copy(email = event.email, error = null) }
            }
            is RegisterUiEvent.PasswordChanged -> {
                _uiState.update { it.copy(password = event.password, error = null) }
            }
            RegisterUiEvent.RegisterClicked -> register()
        }
    }

    private fun register() {
        val state = _uiState.value
        if (state.email.isBlank() || state.password.isBlank() || state.firstName.isBlank() || state.lastName.isBlank()) {
            _uiState.update { it.copy(error = "Please fill in all fields") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            when (val result = authRepository.register(state.email, state.password, state.firstName, state.lastName)) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _uiEffect.send(RegisterUiEffect.NavigateToOtp)
                }
                is AppResult.Error -> {
                    val errorMessage = result.message ?: "Registration failed"
                    _uiState.update { it.copy(isLoading = false, error = errorMessage) }
                    _uiEffect.send(RegisterUiEffect.ShowError(errorMessage))
                }
                AppResult.Loading -> { /* Handled by initial state update */ }
            }
        }
    }
}
