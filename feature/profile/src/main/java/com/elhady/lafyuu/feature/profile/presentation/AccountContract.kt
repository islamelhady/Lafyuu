package com.elhady.lafyuu.feature.profile.presentation

import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.core.designsystem.components.element.TabBarItem
import com.elhady.lafyuu.core.ui.text.UiText

data class AccountUiState(
    val isLoading: Boolean = false,
    val error: UiText? = null
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
    data class ShowSnackbar(val message: UiText, val type: AlertType = AlertType.Error) : AccountUiEffect
}
