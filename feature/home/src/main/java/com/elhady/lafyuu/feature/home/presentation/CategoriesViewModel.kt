package com.elhady.lafyuu.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.feature.home.domain.usecase.GetCategoriesUseCase
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
class CategoriesViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoriesUiState())
    val uiState: StateFlow<CategoriesUiState> = _uiState.asStateFlow()

    private val _uiEffect = Channel<CategoriesUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    init {
        loadCategories()
    }

    fun onEvent(event: CategoriesUiEvent) {
        when (event) {
            CategoriesUiEvent.LoadCategories -> loadCategories()
            is CategoriesUiEvent.SelectCategory -> {
                sendEffect(CategoriesUiEffect.NavigateToCategoryProducts(event.categoryId, event.categoryName))
            }
            CategoriesUiEvent.BackClicked -> sendEffect(CategoriesUiEffect.NavigateBack)
            CategoriesUiEvent.RetryClicked -> loadCategories()
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = getCategoriesUseCase()) {
                is AppResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            categories = result.data,
                            error = null
                        )
                    }
                }
                is AppResult.Error -> {
                    val msg = result.message ?: "Failed to load categories"
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = msg
                        )
                    }
                    sendEffect(CategoriesUiEffect.ShowSnackbar(msg, AlertType.Error))
                }
                is AppResult.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    private fun sendEffect(effect: CategoriesUiEffect) {
        viewModelScope.launch {
            _uiEffect.send(effect)
        }
    }
}
