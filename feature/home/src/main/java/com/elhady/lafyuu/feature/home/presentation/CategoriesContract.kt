package com.elhady.lafyuu.feature.home.presentation

import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.core.ui.text.UiText
import com.elhady.lafyuu.feature.home.presentation.model.CategoryUiModel
import com.elhady.lafyuu.feature.home.presentation.model.ProductUiModel

data class CategoriesUiState(
    val isLoading: Boolean = false,
    val categories: List<CategoryUiModel> = emptyList(),
    val error: UiText? = null
)

sealed interface CategoriesUiIntent {
    data object LoadCategories : CategoriesUiIntent
    data class SelectCategory(val categoryId: String, val categoryName: String) : CategoriesUiIntent
    data object BackClicked : CategoriesUiIntent
    data object RetryClicked : CategoriesUiIntent
}

sealed interface CategoriesUiEffect {
    data object NavigateBack : CategoriesUiEffect
    data class NavigateToCategoryProducts(val categoryId: String, val categoryName: String) : CategoriesUiEffect
    data class ShowSnackbar(val message: UiText, val type: AlertType = AlertType.Error) : CategoriesUiEffect
}
