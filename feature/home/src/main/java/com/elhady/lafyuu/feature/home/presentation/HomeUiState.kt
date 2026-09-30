package com.elhady.lafyuu.feature.home.presentation

import com.elhady.lafyuu.feature.home.domain.model.Category
import com.elhady.lafyuu.feature.home.domain.model.Offer
import com.elhady.lafyuu.feature.home.domain.model.Product

data class HomeUiState(
    val searchQuery: String = "",
    val isLoading: Boolean = true,
    val isSearching: Boolean = false,
    val categories: List<Category> = emptyList(),
    val offers: List<Offer> = emptyList(),
    val flashSaleProducts: List<Product> = emptyList(),
    val megaSaleProducts: List<Product> = emptyList(),
    val recommendedProducts: List<Product> = emptyList(),
    val searchResults: List<Product> = emptyList(),
    val selectedTab: String = "Home",
    val errorMessage: String? = null
) {
    val isContentEmpty: Boolean
        get() = !isLoading && categories.isEmpty() && flashSaleProducts.isEmpty() && megaSaleProducts.isEmpty() && recommendedProducts.isEmpty()
}
