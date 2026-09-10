package com.elhady.lafyuu.core.designsystem.component.text

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.elhady.lafyuu.core.designsystem.theme.LafyuuColor
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTypography

@Composable
fun LafyuuSectionTitle(
    title: String,
    modifier: Modifier = Modifier,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = LafyuuTypography.SectionTitle,
            color = LafyuuColor.TextPrimary
        )

        if (actionText != null && onActionClick != null) {
            TextButton(
                onClick = onActionClick
            ) {
                Text(
                    text = actionText,
                    style = LafyuuTypography.Label
                )
            }
        }
    }
}

@Preview
@Composable
fun LafyuuSectionTitlePreview() {
    LafyuuSectionTitle(
        title = "Section Title",
        actionText = "Action",
        onActionClick = {}
    )
}