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
    isTotal: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        LafyuuText(
            text = label,
            style = if (isTotal) Theme.typography.mediumTextBold else Theme.typography.normalTextRegular,
            color = if (isTotal) Theme.color.neutralDark else Theme.color.neutralGrey
        )
        LafyuuText(
            text = value,
            style = if (isTotal) Theme.typography.mediumTextBold else Theme.typography.normalTextRegular,
            color = if (isTotal) Theme.color.blue else Theme.color.neutralDark
        )
    }
}