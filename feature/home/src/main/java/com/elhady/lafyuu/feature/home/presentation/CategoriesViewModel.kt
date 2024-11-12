package com.elhady.lafyuu.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.core.ui.text.toUiText
import com.elhady.lafyuu.feature.home.domain.model.Category
import com.elhady.lafyuu.feature.home.domain.usecase.GetCategoriesUseCase
import com.elhady.lafyuu.feature.home.presentation.mapper.toUiModel
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
class CategoriesViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoriesUiState())
    val uiState: StateFlow<CategoriesUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<CategoriesUiEffect>(
        replay = 0, extraBufferCapacity = 64, onBufferOverflow = BufferOverflow.SUSPEND
    )
    val uiEffect: SharedFlow<CategoriesUiEffect> = _uiEffect.asSharedFlow()

    init {
        loadCategories()
    }

    fun onEvent(event: CategoriesUiIntent) {
        when (event) {
            CategoriesUiIntent.LoadCategories -> loadCategories()
            is CategoriesUiIntent.SelectCategory -> {
                sendEffect(CategoriesUiEffect.NavigateToCategoryProducts(event.categoryId, event.categoryName))
            }
            CategoriesUiIntent.BackClicked -> sendEffect(CategoriesUiEffect.NavigateBack)
            CategoriesUiIntent.RetryClicked -> loadCategories()
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
                            categories = result.data.map(Category::toUiModel),
                            error = null
                        )
                    }
                }
                is AppResult.Failure -> {
                    val message = result.error.toUiText()
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = message
                        )
                    }
                    sendEffect(CategoriesUiEffect.ShowSnackbar(message, AlertType.Error))
                }
            }
        }
    }

    private fun sendEffect(effect: CategoriesUiEffect) {
        viewModelScope.launch { _uiEffect.emit(effect) }
    }
}
