package com.elhady.lafyuu.core.designsystem.components.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.elhady.lafyuu.core.designsystem.components.text.LafyuuText
import com.elhady.lafyuu.core.designsystem.icons.Alert
import com.elhady.lafyuu.core.designsystem.theme.Theme

@Composable
fun InformationCard(
    modifier: Modifier = Modifier,
    information: String = "Please check your inbox"
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(Theme.corner.small)
            .background(Theme.color.yellow)
            .padding(Theme.space.large)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Theme.space.small)
        ) {
            Icon(
                imageVector = Alert,
                contentDescription = null,
                tint = Theme.color.backgroundWhite
            )
            LafyuuText(
                text = information,
                style = Theme.typography.normalTextRegular,
                color = Theme.color.backgroundWhite,
                textAlign = TextAlign.Start,
            )
        }
    }
}

@Preview
@Composable
fun InformationCardPreview() {
    InformationCard()
}