package com.elhady.lafyuu.core.designsystem.components.other

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.elhady.lafyuu.core.designsystem.components.text.LafyuuText
import com.elhady.lafyuu.core.designsystem.theme.Theme

@Composable
fun NotificationMarkCount(
    badgeCount: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(20.dp)
            .clip(CircleShape)
            .background(Theme.color.red)
            .border(
                width = 2.dp,
                color = Theme.color.backgroundWhite,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        LafyuuText(
            text = badgeCount.toString(),
            style = Theme.typography.normalCaptionBold,
            color = Theme.color.backgroundWhite
        )
    }
}

@Preview
@Composable
fun NotificationMarkCountPreview() {
    NotificationMarkCount(badgeCount = 40)
}
