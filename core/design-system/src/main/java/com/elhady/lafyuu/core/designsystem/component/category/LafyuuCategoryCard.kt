package com.elhady.lafyuu.core.designsystem.component.category

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.elhady.lafyuu.core.designsystem.theme.LafyuuDimens
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTypography

@Composable
fun LafyuuCategoryCard(
    imageUrl: String?,
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            LafyuuDimens.Space2
        ),
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = title,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(
                    RoundedCornerShape(
                        LafyuuDimens.Space4
                    )
                ),
            contentScale = ContentScale.Crop,
        )

        Text(
            text = title,
            style = LafyuuTypography.Body,
            modifier = Modifier.padding(
                horizontal = LafyuuDimens.Space2
            ),
            maxLines = 1,
        )
    }
}

@Preview
@Composable
fun LafyuuCategoryCardPreview() {
    LafyuuCategoryCard(
        imageUrl = null,
        title = "Sample Category",
        onClick = {},
    )
}