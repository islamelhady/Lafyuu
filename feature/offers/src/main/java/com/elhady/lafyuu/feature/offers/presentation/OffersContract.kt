package com.elhady.lafyuu.feature.offers.presentation

import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.core.designsystem.components.element.TabBarItem
import com.elhady.lafyuu.feature.offers.domain.model.Offer

data class OffersUiState(
    val isLoading: Boolean = false,
    val offers: List<Offer> = emptyList(),
    val error: String? = null
)

sealed interface OffersUiEvent {
    data object LoadOffers : OffersUiEvent
    data class OfferClicked(val offer: Offer) : OffersUiEvent
    data object RetryClicked : OffersUiEvent
    data class BottomTabSelected(val tab: TabBarItem) : OffersUiEvent
}

sealed interface OffersUiEffect {
    data class NavigateToTab(val tabRoute: String) : OffersUiEffect
    data class ShowSnackbar(val message: String, val type: AlertType = AlertType.Error) : OffersUiEffect
}
