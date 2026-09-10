package com.elhady.lafyuu.core.designsystem.component.quantity

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.elhady.lafyuu.core.designsystem.theme.LafyuuDimens
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTypography

@Composable
fun LafyuuQuantitySelector(
    quantity: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(
            LafyuuDimens.Space4
        ),
    ) {
        IconButton(
            onClick = onDecrease,
            enabled = quantity > 1,
        ) {
            Icon(
                imageVector = Icons.Default.Remove,
                contentDescription = "Decrease quantity",
            )
        }

        Text(
            text = quantity.toString(),
            style = LafyuuTypography.Body,
            modifier = Modifier.padding(
                horizontal = LafyuuDimens.Space4
            ),
        )

        IconButton(
            onClick = onIncrease,
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Increase quantity",
            )
        }
    }
}

@Preview
@Composable
fun LafyuuQuantitySelectorPreview() {
    LafyuuQuantitySelector(
        quantity = 1,
        onIncrease = {},
        onDecrease = {},
    )
}