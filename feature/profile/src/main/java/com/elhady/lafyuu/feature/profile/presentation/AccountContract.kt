package com.elhady.lafyuu.feature.profile.presentation

import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.core.designsystem.components.element.TabBarItem
import com.elhady.lafyuu.feature.profile.domain.model.UserProfile

data class AccountUiState(
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed interface AccountUiEvent {
    data object ProfileClicked : AccountUiEvent
    data object OrdersClicked : AccountUiEvent
    data object AddressClicked : AccountUiEvent
    data object PaymentClicked : AccountUiEvent
    data class BottomTabSelected(val tab: TabBarItem) : AccountUiEvent
}

sealed interface AccountUiEffect {
    data object NavigateToProfile : AccountUiEffect
    data object NavigateToOrders : AccountUiEffect
    data object NavigateToAddress : AccountUiEffect
    data class NavigateToTab(val tabRoute: String) : AccountUiEffect
    data class ShowSnackbar(val message: String, val type: AlertType = AlertType.Error) : AccountUiEffect
}
