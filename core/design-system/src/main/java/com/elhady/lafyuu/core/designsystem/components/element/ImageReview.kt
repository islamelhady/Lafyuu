package com.elhady.lafyuu.core.designsystem.components.element

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.elhady.lafyuu.core.designsystem.R
import com.elhady.lafyuu.core.designsystem.components.button.IconButton
import com.elhady.lafyuu.core.designsystem.components.text.SectionTitle
import com.elhady.lafyuu.core.designsystem.icons.Plus
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTheme
import com.elhady.lafyuu.core.designsystem.theme.Theme

@Composable
fun ImageReview(
    @DrawableRes imageUrls: List<Int>,
    modifier: Modifier = Modifier,
    onAddClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(Theme.space.medium)
    ) {
        imageUrls.forEach { resId ->
            Image(
                painter = painterResource(id = resId),
                contentDescription = null,
                modifier = Modifier
                    .size(Theme.size.huge)
                    .clip(Theme.corner.small),
                contentScale = ContentScale.Crop
            )
        }
        onAddClick?.let {
            IconButton(
                icon = Plus,
                onClick = it,
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 380, heightDp = 400)
@Composable
private fun AllElementComponentsPreview() {
    LafyuuTheme {
        Surface {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                SectionTitle("Image Review")
                ImageReview(
                    imageUrls = listOf(
                        R.drawable.img_product_shoes_bottom,
                        R.drawable.img_product_shoes_side,
                        R.drawable.img_product_shoes_back
                    ),
                    onAddClick = {})
            }
        }
    }
}