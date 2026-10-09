package com.elhady.lafyuu.feature.explore.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.feature.explore.domain.usecase.GetCategoriesUseCase
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
class ExploreViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExploreUiState())
    val uiState: StateFlow<ExploreUiState> = _uiState.asStateFlow()

    private val _uiEffect = Channel<ExploreUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    init {
        loadCategories()
    }

    fun onEvent(event: ExploreUiEvent) {
        when (event) {
            ExploreUiEvent.LoadCategories -> loadCategories()
            is ExploreUiEvent.CategoryClicked -> {
                sendEffect(ExploreUiEffect.NavigateToCategoryProducts(event.categoryId, event.categoryName))
            }
            ExploreUiEvent.RetryClicked -> loadCategories()
            ExploreUiEvent.NotificationClicked -> {
                sendEffect(ExploreUiEffect.NavigateToNotifications)
            }
            ExploreUiEvent.WishlistClicked -> {
                sendEffect(ExploreUiEffect.NavigateToWishlist)
            }
            ExploreUiEvent.SearchClicked -> {
                sendEffect(ExploreUiEffect.NavigateToSearch)
            }
            is ExploreUiEvent.BottomTabSelected -> {
                sendEffect(ExploreUiEffect.NavigateToTab(event.tab.route))
            }
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = getCategoriesUseCase()) {
                is AppResult.Success -> {
                    val grouped = groupByExploreCategory(categories = result.data)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            groups = grouped,
                            error = null
                        )
                    }
                }
                is AppResult.Error -> {
                    val message = result.message ?: "Failed to load categories"
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = message
                        )
                    }
                    sendEffect(ExploreUiEffect.ShowSnackbar(message, AlertType.Error))
                }
                is AppResult.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    private fun sendEffect(effect: ExploreUiEffect) {
        viewModelScope.launch {
            _uiEffect.send(effect)
        }
    }
}
