package com.elhady.lafyuu.feature.cart.presentation.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.elhady.lafyuu.core.designsystem.components.button.DefaultButton
import com.elhady.lafyuu.core.designsystem.components.card.PriceDetails
import com.elhady.lafyuu.core.designsystem.components.element.VerticalSpacerExtraLarge
import com.elhady.lafyuu.core.designsystem.components.element.VerticalSpacerLarge
import com.elhady.lafyuu.core.designsystem.components.element.VerticalSpacerMedium
import com.elhady.lafyuu.core.designsystem.components.textfield.CouponTextField
import com.elhady.lafyuu.core.designsystem.theme.Theme
import com.elhady.lafyuu.feature.cart.presentation.CartUiEvent
import com.elhady.lafyuu.feature.cart.presentation.CartUiState

@SuppressLint("DefaultLocale")
@Composable
fun CartSummarySection(
    uiState: CartUiState,
    onEvent: (CartUiEvent) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Theme.space.small)
    ) {
        CouponTextField(
            value = uiState.couponCodeInput,
            onValueChange = { onEvent(CartUiEvent.CouponCodeChanged(it)) },
            onApplyClick = { onEvent(CartUiEvent.ApplyCouponClicked) },
            errorMessage = uiState.couponError,
        )
        VerticalSpacerMedium()
        val itemsTotal = uiState.items.sumOf { it.totalPrice }
        val discount = uiState.discountAmount ?: 0.0
        val finalTotal = (uiState.finalTotal ?: itemsTotal) - discount

        PriceDetails(
            itemsCount = uiState.items.sumOf { it.quantity },
            itemsTotal = "$${String.format("%.2f", itemsTotal)}",
            shipping = "$0.00",
            importCharges = "$0.00",
            discount = if (discount > 0) "-${String.format("%.2f", discount)}" else "$0.00",
            totalPrice = "$${String.format("%.2f", if (finalTotal > 0) finalTotal else itemsTotal)}"
        )
        VerticalSpacerLarge()
        DefaultButton(
            caption = "Check Out",
            isLoading = false,
            onClick = { onEvent(CartUiEvent.CheckoutClicked) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
