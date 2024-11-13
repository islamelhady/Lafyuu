package com.elhady.lafyuu.feature.cart.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.elhady.lafyuu.core.designsystem.components.card.PriceDetails
import com.elhady.lafyuu.core.designsystem.components.element.VerticalSpacerLarge
import com.elhady.lafyuu.core.designsystem.components.element.VerticalSpacerMedium
import com.elhady.lafyuu.core.designsystem.components.textfield.CouponTextField
import com.elhady.lafyuu.core.designsystem.theme.Theme
import com.elhady.lafyuu.core.ui.text.UiText
import com.elhady.lafyuu.core.ui.text.asString
import com.elhady.lafyuu.feature.cart.presentation.CartUiIntent
import com.elhady.lafyuu.feature.cart.presentation.model.CartSummaryUi

@Composable
fun CartSummarySection(
    summary: CartSummaryUi,
    couponCodeInput: String,
    couponError: UiText?,
    onIntent: (CartUiIntent) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Theme.space.small)
    ) {
        CouponTextField(
            value = couponCodeInput,
            onValueChange = { onIntent(CartUiIntent.CouponCodeChanged(it)) },
            onApplyClick = { onIntent(CartUiIntent.ApplyCouponClicked) },
            errorMessage = couponError?.asString()
        )
        VerticalSpacerMedium()
        PriceDetails(
            itemsCount = summary.itemsCount,
            itemsTotal = summary.itemsTotal,
            shipping = summary.shipping,
            importCharges = summary.importCharges,
            discount = summary.discount,
            totalPrice = summary.total
        )
        VerticalSpacerLarge()
    }
}