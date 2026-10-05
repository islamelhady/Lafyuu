package com.elhady.lafyuu.feature.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elhady.lafyuu.feature.auth.domain.usecase.LogoutUseCase
import com.elhady.lafyuu.feature.profile.domain.usecase.GetUserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(AccountUiState())
    val uiState: StateFlow<AccountUiState> = _uiState.asStateFlow()

    private val _uiEffect = Channel<AccountUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    init {
        _uiState.value = AccountUiState(isLoading = true)
    }

    fun onEvent(event: AccountUiEvent) {
        when (event) {
            AccountUiEvent.ProfileClicked -> sendEffect(AccountUiEffect.NavigateToProfile)
            AccountUiEvent.OrdersClicked -> sendEffect(AccountUiEffect.NavigateToOrders)
            AccountUiEvent.AddressClicked -> sendEffect(AccountUiEffect.NavigateToAddress)
            AccountUiEvent.PaymentClicked -> sendEffect(AccountUiEffect.NavigateToPayment)
        }
    }

    private fun sendEffect(effect: AccountUiEffect) {
        viewModelScope.launch {
            _uiEffect.send(effect)
        }
    }
}
