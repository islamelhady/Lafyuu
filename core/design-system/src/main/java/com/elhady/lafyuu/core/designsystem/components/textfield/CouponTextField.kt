package com.elhady.lafyuu.core.designsystem.components.textfield

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.elhady.lafyuu.core.designsystem.components.text.LafyuuText
import com.elhady.lafyuu.core.designsystem.components.text.SectionTitle
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTheme
import com.elhady.lafyuu.core.designsystem.theme.Theme

@Composable
fun LafyuuCouponForm(
    value: String,
    onValueChange: (String) -> Unit,
    onApplyClick: () -> Unit,
    modifier: Modifier = Modifier,
    errorMessage: String? = "* Your Coupon Is Not Correct",
    state: LafyuuFieldState = LafyuuFieldState.Default
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        LafyuuTextField(
            value = value,
            onValueChange = onValueChange,
            errorMessage = errorMessage,
            modifier = Modifier
                .weight(1f),
            placeholder = "Enter Coupon Code",
            state = state,
            textAreaSize = Theme.size.buttonHeight,
            shape = RoundedCornerShape(
                topStart = Theme.space.extraSmall,
                bottomStart = Theme.space.extraSmall
            ),
        )

        Button(
            onClick = onApplyClick,
            modifier = Modifier
                .height(Theme.size.buttonHeight)
                .width(Theme.size.smallButtonWidth),
            shape = RoundedCornerShape(
                topEnd = Theme.space.extraSmall,
                bottomEnd = Theme.space.extraSmall
            ),
            colors = ButtonDefaults.buttonColors(
                containerColor = Theme.color.blue
            )
        ) {
            LafyuuText(
                text = "Apply",
                style = Theme.typography.normalTextBold,
                color = Theme.color.backgroundWhite
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun AllRemainingFormsPreview() {
    LafyuuTheme {
        Surface {
            Column(
                modifier = Modifier
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                SectionTitle("Coupon")
                var coupon by remember { mutableStateOf("") }
                LafyuuCouponForm(value = coupon, onValueChange = { coupon = it }, onApplyClick = {})
                var couponError by remember { mutableStateOf("XzOp014524BDH") }
                LafyuuCouponForm(
                    value = couponError,
                    onValueChange = { couponError = it },
                    onApplyClick = {},
                    errorMessage = "* Your Coupon Is Not Correct ",
                    state = LafyuuFieldState.Error
                )
            }
        }
    }
}