package com.elhady.lafyuu.feature.address.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.common.result.AppError
import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.core.ui.text.UiText
import com.elhady.lafyuu.core.ui.text.toUiText
import com.elhady.lafyuu.feature.address.R
import com.elhady.lafyuu.feature.address.domain.model.AddressField
import com.elhady.lafyuu.feature.address.domain.model.AddressFieldError
import com.elhady.lafyuu.feature.address.domain.usecase.CreateAddressUseCase
import com.elhady.lafyuu.feature.address.domain.usecase.GetAddressUseCase
import com.elhady.lafyuu.feature.address.domain.usecase.UpdateAddressUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
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
class AddressFormViewModel @Inject constructor(
    private val getAddressUseCase: GetAddressUseCase,
    private val createAddressUseCase: CreateAddressUseCase,
    private val updateAddressUseCase: UpdateAddressUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // null = add mode (the add route has no argument)
    private val addressId: String? =
        savedStateHandle.get<String>(ADDRESS_ID_ARG)?.takeIf { it.isNotBlank() }

    private val _uiState = MutableStateFlow(
        AddressFormUiState(isEditMode = addressId != null, isFetching = addressId != null)
    )
    val uiState: StateFlow<AddressFormUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<AddressFormUiEffect>(
        replay = 0,
        extraBufferCapacity = 64,
        onBufferOverflow = BufferOverflow.SUSPEND
    )
    val uiEffect: SharedFlow<AddressFormUiEffect> = _uiEffect.asSharedFlow()

    init {
        if (addressId != null) loadAddress(addressId)
    }

    fun onIntent(intent: AddressFormUiIntent) {
        when (intent) {
            is AddressFormUiIntent.FieldChanged -> _uiState.update {
                it.copy(
                    form = it.form.update(intent.field, intent.value),
                    errors = it.errors - intent.field
                )
            }

            AddressFormUiIntent.SaveClicked -> save()
            AddressFormUiIntent.RetryLoadClicked -> addressId?.let(::loadAddress)
            AddressFormUiIntent.BackClicked -> sendEffect(AddressFormUiEffect.NavigateBack)
        }
    }

    private fun loadAddress(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isFetching = true, loadError = null) }
            when (val result = getAddressUseCase(id)) {
                is AppResult.Success -> _uiState.update {
                    it.copy(isFetching = false, form = result.data.toForm())
                }

                // shown as a full screen error with retry, no snackbar on top of it
                is AppResult.Failure -> _uiState.update {
                    it.copy(isFetching = false, loadError = result.error.toUiText())
                }
            }
        }
    }

    private fun save() {
        val state = _uiState.value
        if (state.isSaving || state.isFetching || state.loadError != null) return

        val input = state.form.toInput().normalized()
        val validationErrors = input.validate()
        if (validationErrors.isNotEmpty()) {
            _uiState.update { it.copy(errors = validationErrors.mapValues { (_, e) -> e.toUiText() }) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, errors = emptyMap()) }
            val result = if (addressId == null) {
                createAddressUseCase(input)
            } else {
                updateAddressUseCase(addressId, input)
            }
            when (result) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(isSaving = false) }
                    _uiEffect.emit(AddressFormUiEffect.NavigateBack)
                }

                is AppResult.Failure -> {
                    val serverErrors = (result.error as? AppError.Validation)
                        ?.fieldErrors.orEmpty().toFieldErrors()
                    if (serverErrors.isNotEmpty()) {
                        _uiState.update { it.copy(isSaving = false, errors = serverErrors) }
                    } else {
                        _uiState.update { it.copy(isSaving = false) }
                        _uiEffect.emit(
                            AddressFormUiEffect.ShowSnackbar(result.error.toUiText(), AlertType.Error)
                        )
                    }
                }
            }
        }
    }

    private fun sendEffect(effect: AddressFormUiEffect) {
        viewModelScope.launch { _uiEffect.emit(effect) }
    }

    private fun AddressFieldError.toUiText(): UiText = when (this) {
        AddressFieldError.EMPTY -> UiText.Resource(R.string.error_empty_field)
        AddressFieldError.INVALID_PHONE -> UiText.Resource(R.string.error_invalid_phone)
    }

    // server keys can come in any casing ("PhoneNumber"), only known fields are shown inline
    private fun Map<String, List<String>>.toFieldErrors(): Map<AddressField, UiText> =
        entries.mapNotNull { (key, messages) ->
            val field = AddressField.entries.firstOrNull { it.apiName.equals(key, ignoreCase = true) }
            val message = messages.firstOrNull { it.isNotBlank() }
            if (field != null && message != null) field to UiText.Dynamic(message) else null
        }.toMap()

    companion object {
        const val ADDRESS_ID_ARG = "addressId"
    }
}