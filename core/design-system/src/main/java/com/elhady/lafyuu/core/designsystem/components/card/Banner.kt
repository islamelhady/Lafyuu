package com.elhady.lafyuu.core.designsystem.components.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.elhady.lafyuu.core.designsystem.R
import com.elhady.lafyuu.core.designsystem.components.text.LafyuuText
import com.elhady.lafyuu.core.designsystem.theme.Theme

import coil.compose.AsyncImage

@Composable
fun Banner(
    title: String = "Super Flash Sale",
    subtitle: String? = null,
    imageUrl: Any? = R.drawable.img_promo_shoes_red,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = { onClick?.invoke() },
        enabled = onClick != null,
        modifier = modifier
            .fillMaxWidth()
            .height(208.dp),
        shape = Theme.corner.small,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            AsyncImage(
                model = imageUrl ?: R.drawable.img_promo_shoes_red,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.img_promo_shoes_red),
                error = painterResource(id = R.drawable.img_promo_shoes_red),
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.25f))
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = Theme.space.extraLarge, vertical = Theme.space.huge),
                verticalArrangement = Arrangement.Center
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(Theme.space.small)
                ) {
                    LafyuuText(
                        text = title,
                        modifier = Modifier.width(209.dp),
                        style = Theme.typography.heading2,
                        color = Theme.color.backgroundWhite,
                        maxLines = 2
                    )
                    subtitle?.let {
                        LafyuuText(
                            text = it,
                            style = Theme.typography.normalTextRegular,
                            color = Theme.color.backgroundWhite
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun BannerPreview() {
    Banner(
        title = "Super Flash Sale 50% Off",
        subtitle = "We recommend the best for you",
        imageUrl = R.drawable.img_promo_shoes_close_up
    )
}