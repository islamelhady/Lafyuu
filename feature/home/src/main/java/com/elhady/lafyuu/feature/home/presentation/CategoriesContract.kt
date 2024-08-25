package com.elhady.lafyuu.feature.home.presentation

import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.feature.home.domain.model.Category

data class CategoriesUiState(
    val isLoading: Boolean = false,
    val categories: List<Category> = emptyList(),
    val error: String? = null
)

sealed interface CategoriesUiEvent {
    data object LoadCategories : CategoriesUiEvent
    data class SelectCategory(val categoryId: String, val categoryName: String) : CategoriesUiEvent
    data object BackClicked : CategoriesUiEvent
    data object RetryClicked : CategoriesUiEvent
}

sealed interface CategoriesUiEffect {
    data object NavigateBack : CategoriesUiEffect
    data class NavigateToCategoryProducts(val categoryId: String, val categoryName: String) : CategoriesUiEffect
    data class ShowSnackbar(val message: String, val type: AlertType = AlertType.Error) : CategoriesUiEffect
}
