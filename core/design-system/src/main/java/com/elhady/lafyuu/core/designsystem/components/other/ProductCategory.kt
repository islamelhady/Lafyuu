package com.elhady.lafyuu.core.designsystem.components.other

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.elhady.lafyuu.core.designsystem.components.text.LafyuuText
import com.elhady.lafyuu.core.designsystem.icons.ManUnderwear
import com.elhady.lafyuu.core.designsystem.icons.Shirt
import com.elhady.lafyuu.core.designsystem.icons.WomanBag
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTheme
import com.elhady.lafyuu.core.designsystem.theme.Theme

@Composable
fun ProductCategory(
    label: String,
    icon: ImageVector?,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier.width(Theme.size.huge),
        shape = Theme.corner.small,
        colors = CardDefaults.cardColors(containerColor = Theme.color.backgroundWhite),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Theme.space.small)
        ) {
            Box(
                modifier = Modifier
                    .size(Theme.size.huge)
                    .clip(CircleShape)
                    .background(Theme.color.backgroundWhite)
                    .border(Theme.size.border, Theme.color.neutralLight, CircleShape)
                    .background(Theme.color.backgroundWhite),
                contentAlignment = Alignment.Center
            ) {
                icon?.let {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        tint = Theme.color.blue,
                        modifier = Modifier.size(Theme.size.iconLarge)
                    )
                }
            }
            LafyuuText(
                text = label,
                style = Theme.typography.normalTextRegular,
                color = Theme.color.neutralGrey,
                textAlign = TextAlign.Center,
                maxLines = 2,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProductCategoryPreview() {
    LafyuuTheme {
        Row(
            modifier = Modifier
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(Theme.space.medium)
        ) {
            ProductCategory(
                label = "Man underwear",
                icon = ManUnderwear,
                onClick = {}
            )
            ProductCategory(
                label = "Shirt",
                icon = Shirt,
                onClick = {}
            )
            ProductCategory(
                label = "Woman Bag",
                icon = WomanBag,
                onClick = {}
            )
        }
    }
}