package com.elhady.lafyuu.feature.offers.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.feature.offers.domain.usecase.GetOffersUseCase
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
class OffersViewModel @Inject constructor(
    private val getOffersUseCase: GetOffersUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(OffersUiState())
    val uiState: StateFlow<OffersUiState> = _uiState.asStateFlow()

    private val _uiEffect = Channel<OffersUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    init {
        loadOffers()
    }

    fun onEvent(event: OffersUiEvent) {
        when (event) {
            OffersUiEvent.LoadOffers -> loadOffers()
            is OffersUiEvent.OfferClicked -> {
                val message = buildString {
                    append(event.offer.name)
                    if (!event.offer.description.isNullOrBlank()) {
                        append(" - ")
                        append(event.offer.description)
                    }
                }
                sendEffect(OffersUiEffect.ShowSnackbar(message, AlertType.Success))
            }
            OffersUiEvent.RetryClicked -> loadOffers()
            is OffersUiEvent.BottomTabSelected -> {
                sendEffect(OffersUiEffect.NavigateToTab(event.tab.route))
            }
        }
    }

    private fun loadOffers() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = getOffersUseCase()) {
                is AppResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            offers = result.data,
                            error = null
                        )
                    }
                }
                is AppResult.Error -> {
                    val message = result.message ?: "Failed to load offers"
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = message
                        )
                    }
                    sendEffect(OffersUiEffect.ShowSnackbar(message, AlertType.Error))
                }
                is AppResult.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    private fun sendEffect(effect: OffersUiEffect) {
        viewModelScope.launch {
            _uiEffect.send(effect)
        }
    }
}
