package com.elhady.lafyuu.core.designsystem.component.promo

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.elhady.lafyuu.core.designsystem.theme.LafyuuDimens
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTypography
import androidx.compose.ui.Modifier

@Composable
fun LafyuuPromoBanner(
    title: String,
    subtitle: String? = null,
    imageUrl: String? = null,
    actionText: String? = null,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(2f)
            .clip(
                RoundedCornerShape(
                    LafyuuDimens.Space8
                )
            )
            .then(
                if (onClick != null) {
                    Modifier.clickable(onClick = onClick)
                } else {
                    Modifier
                }
            )
    ) {
        if (!imageUrl.isNullOrBlank()) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop,
            )
        } else {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        MaterialTheme.colorScheme.primaryContainer
                    )
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(LafyuuDimens.Space8),
        ) {
            Text(
                text = title,
                style = LafyuuTypography.SectionTitle,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )

            if (!subtitle.isNullOrBlank()) {
                Text(
                    text = subtitle,
                    style = LafyuuTypography.Title,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(
                        top = LafyuuDimens.Space2
                    ),
                )
            }

            if (!actionText.isNullOrBlank()) {
                Text(
                    text = actionText,
                    style = LafyuuTypography.Body,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(
                        top = LafyuuDimens.Space4
                    ),
                )
            }
        }
    }
}

@Preview
@Composable
fun LafyuuPromoBannerPreview(){
    LafyuuPromoBanner(
        title = "Sample Title",
        subtitle = "Sample Subtitle",
        imageUrl = null,
        actionText = "Action",
        onClick = {},
    )
}