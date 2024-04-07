package com.elhady.lafyuu.core.designsystem.components.element

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.elhady.lafyuu.core.designsystem.icons.Check
import com.elhady.lafyuu.core.designsystem.icons.Close
import com.elhady.lafyuu.core.designsystem.icons.Warning
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTheme
import com.elhady.lafyuu.core.designsystem.theme.Theme

enum class AlertType { Success, Cancel, Warning }

@Composable
fun AlertIcon(
    type: AlertType,
    modifier: Modifier = Modifier
) {
    val (color, icon) = when (type) {
        AlertType.Success -> Theme.color.green to Check
        AlertType.Cancel -> Theme.color.error to Close
        AlertType.Warning -> Theme.color.yellow to Warning
    }

    Box(
        modifier = modifier
            .dropShadow(
                shape = CircleShape,
                shadow = Shadow(
                    radius = 30.dp,
                    offset = DpOffset(x = 0.dp, y = 10.dp),
                    color = color.copy(alpha = 0.24f)
                )
            )
            .size(Theme.size.huge)
            .background(color, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = type.name,
            tint = Theme.color.backgroundWhite
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AlertIconPreview() {
    LafyuuTheme {
        Column(
            modifier = Modifier.padding(Theme.space.large),
            verticalArrangement = Arrangement.spacedBy(Theme.space.small)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(Theme.space.small)) {
                AlertIcon(type = AlertType.Success)
                AlertIcon(type = AlertType.Cancel)
                AlertIcon(type = AlertType.Warning)
            }
        }
    }
}