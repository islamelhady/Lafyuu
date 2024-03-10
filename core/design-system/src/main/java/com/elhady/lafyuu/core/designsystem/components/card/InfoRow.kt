package com.elhady.lafyuu.core.designsystem.components.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.elhady.lafyuu.core.designsystem.components.text.LafyuuText
import com.elhady.lafyuu.core.designsystem.theme.Theme

@Composable
fun InfoRow(
    label: String,
    value: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        LafyuuText(
            text = label,
            style = Theme.typography.normalTextRegular,
            color = Theme.color.neutralGrey
        )
        LafyuuText(
            text = value,
            style = Theme.typography.normalTextRegular,
            color = Theme.color.neutralDark
        )
    }
}