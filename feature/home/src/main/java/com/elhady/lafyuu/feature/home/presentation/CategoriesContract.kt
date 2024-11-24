package com.elhady.lafyuu.feature.home.presentation

import com.elhady.lafyuu.core.ui.text.UiText
import com.elhady.lafyuu.feature.home.presentation.model.CategoryUiModel

data class CategoriesUiState(
    val isLoading: Boolean = false,
    val categories: List<CategoryUiModel> = emptyList(),
    val error: UiText? = null
)

sealed interface CategoriesUiIntent {
    data class CategoryClicked(val categoryId: String, val categoryName: String) : CategoriesUiIntent
    data object BackClicked : CategoriesUiIntent
    data object RetryClicked : CategoriesUiIntent
}

sealed interface CategoriesUiEffect {
    data object NavigateBack : CategoriesUiEffect
    data class NavigateToCategoryProducts(val categoryId: String, val categoryName: String) : CategoriesUiEffect
}
