package com.elhady.lafyuu.feature.cart.presentation

import com.elhady.lafyuu.core.designsystem.components.element.TabBarItem
import com.elhady.lafyuu.feature.cart.domain.model.Cart
import com.elhady.lafyuu.feature.cart.domain.model.CartItem

data class CartUiState(
    val isLoading: Boolean = false,
    val cart: Cart? = null,
    val items: List<CartItem> = emptyList(),
    val couponCodeInput: String = "",
    val isApplyingCoupon: Boolean = false,
    val couponError: String? = null,
    val updatingItemIds: Set<String> = emptySet(),
    val error: String? = null,
    val originalTotal: Double? = null,
    val discountAmount: Double? = null,
    val finalTotal: Double? = null,
    val appliedCouponCode: String? = null
)

sealed interface CartUiEvent {
    data object LoadCart : CartUiEvent
    data class IncreaseQuantity(val itemId: String, val currentQuantity: Int) : CartUiEvent
    data class DecreaseQuantity(val itemId: String, val currentQuantity: Int) : CartUiEvent
    data class RemoveItem(val itemId: String) : CartUiEvent
    data class CouponCodeChanged(val code: String) : CartUiEvent
    data object ApplyCouponClicked : CartUiEvent
    data object CheckoutClicked : CartUiEvent
    data object RetryClicked : CartUiEvent
    data class ProductClicked(val productId: String) : CartUiEvent
    data class BottomTabSelected(val tab: TabBarItem) : CartUiEvent
}

sealed interface CartUiEffect {
    data object NavigateToCheckout : CartUiEffect
    data class NavigateToProductDetails(val productId: String) : CartUiEffect
    data class NavigateToTab(val tabRoute: String) : CartUiEffect
    data class ShowSnackbar(val message: String) : CartUiEffect
}
