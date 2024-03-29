package com.elhady.lafyuu.feature.home.presentation

data class HomeUiState(
    val searchQuery: String = "",
    val isLoading: Boolean = false
)
