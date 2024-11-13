package com.elhady.lafyuu.feature.cart.presentation

import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.core.designsystem.components.element.TabBarItem
import com.elhady.lafyuu.core.ui.text.UiText
import com.elhady.lafyuu.feature.cart.presentation.model.CartItemUi
import com.elhady.lafyuu.feature.cart.presentation.model.CartUi

data class CartUiState(
    val isLoading: Boolean = true,
    val cart: CartUi? = null,
    val couponCodeInput: String = "",
    val isApplyingCoupon: Boolean = false,
    val couponError: UiText? = null,
    val updatingItemIds: Set<String> = emptySet(),
    val favoriteProductIds: Set<String> = emptySet(),
    val error: UiText? = null
) {
    val items: List<CartItemUi> get() = cart?.items.orEmpty()
    val totalQuantity: Int get() = cart?.summary?.itemsCount ?: 0
}

sealed interface CartUiIntent {
    data object RetryClicked : CartUiIntent
    data class IncreaseQuantity(val itemId: String) : CartUiIntent
    data class DecreaseQuantity(val itemId: String) : CartUiIntent
    data class RemoveItem(val itemId: String) : CartUiIntent
    data class CouponCodeChanged(val code: String) : CartUiIntent
    data object ApplyCouponClicked : CartUiIntent
    data object CheckoutClicked : CartUiIntent
    data class ProductClicked(val productId: String) : CartUiIntent
    data class FavoriteClicked(val productId: String) : CartUiIntent
    data class BottomTabSelected(val tab: TabBarItem) : CartUiIntent
}

sealed interface CartUiEffect {
    data object NavigateToCheckout : CartUiEffect
    data class NavigateToProductDetails(val productId: String) : CartUiEffect
    data class NavigateToTab(val tabRoute: String) : CartUiEffect
    data class ShowSnackbar(val message: UiText, val type: AlertType = AlertType.Success) : CartUiEffect
}