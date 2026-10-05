package com.elhady.lafyuu.feature.orders.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.feature.orders.domain.usecase.GetOrdersUseCase
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
class OrdersViewModel @Inject constructor(
    private val getOrdersUseCase: GetOrdersUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrdersUiState())
    val uiState: StateFlow<OrdersUiState> = _uiState.asStateFlow()

    private val _uiEffect = Channel<OrdersUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    init {
        loadOrders()
    }

    fun onEvent(event: OrdersUiEvent) {
        when (event) {
            OrdersUiEvent.LoadOrders -> loadOrders()
            is OrdersUiEvent.SelectOrder -> {
                sendEffect(OrdersUiEffect.NavigateToOrderDetails(event.orderId))
            }
            OrdersUiEvent.BackClicked -> sendEffect(OrdersUiEffect.NavigateBack)
            OrdersUiEvent.RetryClicked -> loadOrders()
        }
    }

    fun loadOrders() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = getOrdersUseCase()) {
                is AppResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            orders = result.data,
                            error = null
                        )
                    }
                }
                is AppResult.Error -> {
                    val errorMessage = result.message ?: "Failed to load orders"
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = errorMessage
                        )
                    }
                    sendEffect(OrdersUiEffect.ShowSnackbar(message = errorMessage, type = AlertType.Error))
                }
                is AppResult.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    private fun sendEffect(effect: OrdersUiEffect) {
        viewModelScope.launch {
            _uiEffect.send(effect)
        }
    }
}
