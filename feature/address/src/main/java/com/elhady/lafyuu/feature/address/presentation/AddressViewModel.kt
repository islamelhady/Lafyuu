package com.elhady.lafyuu.feature.address.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.core.ui.text.UiText
import com.elhady.lafyuu.core.ui.text.toUiText
import com.elhady.lafyuu.feature.address.R
import com.elhady.lafyuu.feature.address.domain.usecase.DeleteAddressUseCase
import com.elhady.lafyuu.feature.address.domain.usecase.GetAddressesUseCase
import com.elhady.lafyuu.feature.address.presentation.mapper.toUi
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
class AddressViewModel @Inject constructor(
    private val getAddressesUseCase: GetAddressesUseCase,
    private val deleteAddressUseCase: DeleteAddressUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddressUiState())
    val uiState: StateFlow<AddressUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<AddressUiEffect>(
        replay = 0,
        extraBufferCapacity = 64,
        onBufferOverflow = BufferOverflow.SUSPEND
    )
    val uiEffect: SharedFlow<AddressUiEffect> = _uiEffect.asSharedFlow()

    private var loadJob: Job? = null

    fun onIntent(intent: AddressUiIntent) {
        when (intent) {
            AddressUiIntent.Refresh, AddressUiIntent.RetryClicked -> loadAddresses()
            is AddressUiIntent.AddressSelected ->
                _uiState.update { it.copy(selectedAddressId = intent.addressId) }

            is AddressUiIntent.EditAddressClicked ->
                sendEffect(AddressUiEffect.NavigateToEditAddress(intent.addressId))

            is AddressUiIntent.DeleteAddressClicked -> _uiState.update { state ->
                state.copy(pendingDelete = state.addresses.firstOrNull { it.id == intent.addressId })
            }

            AddressUiIntent.DeleteConfirmed -> confirmDelete()
            AddressUiIntent.DeleteDismissed ->
                _uiState.update { if (it.isDeleting) it else it.copy(pendingDelete = null) }

            AddressUiIntent.AddAddressClicked -> sendEffect(AddressUiEffect.NavigateToAddAddress)
            AddressUiIntent.BackClicked -> sendEffect(AddressUiEffect.NavigateBack)
        }
    }

    private fun loadAddresses() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            // spinner only when there is nothing to show yet
            _uiState.update { it.copy(isLoading = it.addresses.isEmpty(), error = null) }
            when (val result = getAddressesUseCase()) {
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
                        _uiEffect.emit(
                            AddressUiEffect.ShowSnackbar(
                                message,
                                type = AlertType.Error
                            )
                        )
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
            when (val result = deleteAddressUseCase(target.id)) {
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
                        AddressUiEffect.ShowSnackbar(
                            message = UiText.Resource(R.string.address_deleted_successfully),
                            type = AlertType.Success
                        )
                    )
                }

                is AppResult.Failure -> {
                    _uiState.update { it.copy(isDeleting = false, pendingDelete = null) }
                    _uiEffect.emit(
                        AddressUiEffect.ShowSnackbar(
                            message = result.error.toUiText(),
                            type = AlertType.Error
                        )
                    )
                }
            }
        }
    }

    private fun sendEffect(effect: AddressUiEffect) {
        viewModelScope.launch { _uiEffect.emit(effect) }
    }
}
