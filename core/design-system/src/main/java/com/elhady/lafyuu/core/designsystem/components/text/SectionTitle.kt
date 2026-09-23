package com.elhady.lafyuu.core.designsystem.components.text

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import com.elhady.lafyuu.core.designsystem.theme.Theme

@Composable
fun SectionTitle(
    title: String,
    modifier: Modifier = Modifier,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Theme.space.large, vertical = Theme.space.small),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        LafyuuText(
            text = title,
            style = Theme.typography.heading5,
            color = Theme.color.neutralDark
        )

        if (actionText != null && onActionClick != null) {
            LafyuuText(
                text = actionText,
                style = Theme.typography.largeLinkRegular,
                color = Theme.color.blue,
                onClick = onActionClick
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SectionTitlePreview() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        SectionTitle(
            title = "Mega Sale",
            actionText = "See More",
            onActionClick = {}
        )
        SectionTitle(
            title = "You Might Also Like",
        )
    }
}