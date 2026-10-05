package com.elhady.lafyuu.feature.orders.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.feature.orders.domain.usecase.GetOrderHistoryUseCase
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
class OrderDetailsViewModel @Inject constructor(
    private val getOrderHistoryUseCase: GetOrderHistoryUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val orderIdArg: String = savedStateHandle["orderId"] ?: ""

    private val _uiState = MutableStateFlow(OrderDetailsUiState(orderId = orderIdArg))
    val uiState: StateFlow<OrderDetailsUiState> = _uiState.asStateFlow()

    private val _uiEffect = Channel<OrderDetailsUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    init {
        if (orderIdArg.isNotBlank()) {
            loadOrderDetails(orderIdArg)
        }
    }

    fun onEvent(event: OrderDetailsUiEvent) {
        when (event) {
            is OrderDetailsUiEvent.LoadOrderDetails -> loadOrderDetails(event.orderId)
            OrderDetailsUiEvent.BackClicked -> sendEffect(OrderDetailsUiEffect.NavigateBack)
            OrderDetailsUiEvent.RetryClicked -> loadOrderDetails(orderIdArg)
        }
    }

    private fun loadOrderDetails(orderId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = getOrderHistoryUseCase(orderId)) {
                is AppResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            orderHistory = result.data,
                            error = null
                        )
                    }
                }
                is AppResult.Error -> {
                    val msg = result.message ?: "Failed to load order details"
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = msg
                        )
                    }
                    sendEffect(OrderDetailsUiEffect.ShowSnackbar(msg, AlertType.Error))
                }
                is AppResult.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    private fun sendEffect(effect: OrderDetailsUiEffect) {
        viewModelScope.launch {
            _uiEffect.send(effect)
        }
    }
}
