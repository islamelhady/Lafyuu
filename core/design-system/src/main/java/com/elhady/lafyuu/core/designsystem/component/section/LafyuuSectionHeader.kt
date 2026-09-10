package com.elhady.lafyuu.core.designsystem.component.section

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import com.elhady.lafyuu.core.designsystem.theme.LafyuuDimens
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTypography

@Composable
fun LafyuuSectionHeader(
    title: String,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                vertical = LafyuuDimens.Space4
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            style = LafyuuTypography.Title,
            modifier = Modifier.weight(1f),
        )

        if (
            !actionText.isNullOrBlank() &&
            onActionClick != null
        ) {
            Text(
                text = actionText,
                style = LafyuuTypography.Body,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable(
                    onClick = onActionClick
                ),
            )
        }
    }
}

@Preview
@Composable
fun LafyuuSectionHeaderPreview() {
    LafyuuSectionHeader(
        title = "Sample Section",
        actionText = "View All",
        onActionClick = {},
    )
}