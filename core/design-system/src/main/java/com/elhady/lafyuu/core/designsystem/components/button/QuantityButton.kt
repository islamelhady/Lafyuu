package com.elhady.lafyuu.core.designsystem.components.button


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.elhady.lafyuu.core.designsystem.icons.MinusMini
import com.elhady.lafyuu.core.designsystem.icons.PlusMini
import com.elhady.lafyuu.core.designsystem.theme.Theme

@Composable
fun LafyuuQuantityButton(
    quantity: Int,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .size(height = Theme.size.small, width = Theme.size.quantityButtonWidth)
            .border(
                width = Theme.size.border,
                color = Theme.color.neutralLight,
                shape = Theme.corner.medium
            )
            .clip(Theme.corner.medium),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        QuantityButton(
            icon = MinusMini,
            onClick = onDecrease,
            modifier = Modifier
                .width(Theme.size.medium)
                .fillMaxHeight(),
        )
        Box(
            modifier = Modifier
                .width(Theme.size.quantityLabelWidth)
                .fillMaxHeight()
                .background(
                    color = Theme.color.neutralLight,
                ),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = quantity.toString(),
                style = Theme.typography.largeCaptionRegular12,
                color = Theme.color.neutralGrey,
            )
        }
        QuantityButton(
            icon = PlusMini,
            onClick = onIncrease,
            modifier = Modifier
                .width(Theme.size.medium)
                .fillMaxHeight()
        )
    }
}

@Composable
private fun QuantityButton(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier,
        borderColor = Color.Transparent,
        icon = icon,
        shape = Theme.corner.none
    )
}

@Preview(showBackground = true)
@Composable
fun LafyuuQuantityButtonPreview() {
    var num: Int by remember { mutableIntStateOf(0) }
    LafyuuQuantityButton(
        quantity = num,
        onDecrease = {
            num--
        },
        onIncrease = {
            num++
        },
    )
}
