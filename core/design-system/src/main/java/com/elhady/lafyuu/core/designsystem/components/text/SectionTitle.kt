package com.elhady.lafyuu.core.designsystem.components.text

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.elhady.lafyuu.core.designsystem.theme.Theme

@Composable
fun SectionTitle(
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
            style = Theme.typography.heading5,
            color = Theme.color.neutralDark
        )

        if (actionText != null && onActionClick != null) {
            TextButton(
                onClick = onActionClick
            ) {
                Text(
                    text = actionText,
                    style = Theme.typography.largeLinkBold,
                    color = Theme.color.blue
                )
            }
        }
    }
}