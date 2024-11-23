package com.elhady.lafyuu.feature.checkout.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.core.ui.text.UiText
import com.elhady.lafyuu.core.ui.text.toUiText
import com.elhady.lafyuu.feature.checkout.R
import com.elhady.lafyuu.feature.checkout.domain.repository.CheckoutAddressGateway
import com.elhady.lafyuu.feature.checkout.presentation.mapper.toUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val addressGateway: CheckoutAddressGateway
) : ViewModel() {

    private val _uiState = MutableStateFlow(CheckoutUiState())
    val uiState: StateFlow<CheckoutUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<CheckoutUiEffect>(
        replay = 0,
        extraBufferCapacity = 64,
        onBufferOverflow = BufferOverflow.SUSPEND
    )
    val uiEffect: SharedFlow<CheckoutUiEffect> = _uiEffect.asSharedFlow()

    private var loadJob: Job? = null

    fun onIntent(intent: CheckoutUiIntent) {
        when (intent) {
            CheckoutUiIntent.Refresh, CheckoutUiIntent.RetryClicked -> loadAddresses()
            is CheckoutUiIntent.AddressSelected ->
                _uiState.update { it.copy(selectedAddressId = intent.addressId) }

            CheckoutUiIntent.AddAddressClicked -> sendEffect(CheckoutUiEffect.NavigateToAddAddress)
            is CheckoutUiIntent.EditAddressClicked ->
                sendEffect(CheckoutUiEffect.NavigateToEditAddress(intent.addressId))

            is CheckoutUiIntent.DeleteAddressClicked -> _uiState.update { state ->
                state.copy(pendingDelete = state.addresses.firstOrNull { it.id == intent.addressId })
            }

            CheckoutUiIntent.DeleteConfirmed -> confirmDelete()
            CheckoutUiIntent.DeleteDismissed ->
                _uiState.update { if (it.isDeleting) it else it.copy(pendingDelete = null) }

            CheckoutUiIntent.NextClicked -> proceedToPayment()
            CheckoutUiIntent.BackClicked -> sendEffect(CheckoutUiEffect.NavigateBack)
        }
    }

    private fun loadAddresses() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            // spinner only when there is nothing to show yet
            _uiState.update { it.copy(isLoading = it.addresses.isEmpty(), error = null) }
            when (val result = addressGateway.getAddresses()) {
                is AppResult.Success -> {
                    val addresses = result.data.map { it.toUi() }
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            addresses = addresses,
                            selectedAddressId = state.selectedAddressId
                                ?.takeIf { id -> addresses.any { it.id == id } }
                                ?: addresses.firstOrNull()?.id
                        )
                    }
                }

                is AppResult.Failure -> {
                    val message = result.error.toUiText()
                    if (_uiState.value.addresses.isEmpty()) {
                        _uiState.update { it.copy(isLoading = false, error = message) }
                    } else {
                        _uiState.update { it.copy(isLoading = false) }
                        _uiEffect.emit(CheckoutUiEffect.ShowSnackbar(message, AlertType.Error))
                    }
                }
            }
        }
    }

    private fun confirmDelete() {
        val state = _uiState.value
        val target = state.pendingDelete ?: return
        if (state.isDeleting) return

        viewModelScope.launch {
            _uiState.update { it.copy(isDeleting = true) }
            when (val result = addressGateway.deleteAddress(target.id)) {
                is AppResult.Success -> {
                    _uiState.update { current ->
                        val remaining = current.addresses.filterNot { it.id == target.id }
                        current.copy(
                            isDeleting = false,
                            pendingDelete = null,
                            addresses = remaining,
                            selectedAddressId = current.selectedAddressId
                                ?.takeIf { id -> remaining.any { it.id == id } }
                                ?: remaining.firstOrNull()?.id
                        )
                    }
                    _uiEffect.emit(
                        CheckoutUiEffect.ShowSnackbar(
                            UiText.Resource(R.string.address_deleted),
                            AlertType.Success
                        )
                    )
                }

                is AppResult.Failure -> {
                    _uiState.update { it.copy(isDeleting = false, pendingDelete = null) }
                    _uiEffect.emit(
                        CheckoutUiEffect.ShowSnackbar(result.error.toUiText(), AlertType.Error)
                    )
                }
            }
        }
    }

    private fun proceedToPayment() {
        val addressId = _uiState.value.selectedAddressId
        if (addressId == null) {
            sendEffect(
                CheckoutUiEffect.ShowSnackbar(
                    UiText.Resource(R.string.please_select_shipping_address),
                    AlertType.Error
                )
            )
            return
        }
        sendEffect(CheckoutUiEffect.NavigateToPayment(addressId))
    }

    private fun sendEffect(effect: CheckoutUiEffect) {
        viewModelScope.launch { _uiEffect.emit(effect) }
    }
}