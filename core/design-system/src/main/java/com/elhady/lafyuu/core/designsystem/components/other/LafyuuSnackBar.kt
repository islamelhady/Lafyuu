package com.elhady.lafyuu.core.designsystem.components.other

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.core.designsystem.components.text.LafyuuText
import com.elhady.lafyuu.core.designsystem.icons.Check
import com.elhady.lafyuu.core.designsystem.icons.Close
import com.elhady.lafyuu.core.designsystem.icons.Warning
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTheme
import com.elhady.lafyuu.core.designsystem.theme.Theme

@Composable
fun LafyuuSnackBar(
    message: String,
    type: AlertType,
    modifier: Modifier = Modifier
) {
    val (color, icon) = when (type) {
        AlertType.Success -> Theme.color.green to Check
        AlertType.Error -> Theme.color.error to Close
        AlertType.Warning -> Theme.color.yellow to Warning
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .dropShadow(
                shape = CircleShape,
                shadow = Shadow(
                    radius = 30.dp,
                    offset = DpOffset(x = 0.dp, y = 10.dp),
                    color = color.copy(alpha = 0.24f)
                )
            )
            .clip(Theme.corner.small)
            .background(color)
            .padding(Theme.space.large),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Theme.space.medium)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = type.name,
            tint = Theme.color.backgroundWhite,
            modifier = Modifier.size(Theme.size.iconMedium)
        )
        LafyuuText(
            text = message,
            style = Theme.typography.normalTextBold,
            color = Theme.color.backgroundWhite,
            modifier = Modifier.weight(1f)
        )
    }
}


data class LafyuuSnackBarVisuals(
    override val message: String,
    val type: AlertType,
    override val actionLabel: String? = null,
    override val withDismissAction: Boolean = false,
    override val duration: SnackbarDuration = SnackbarDuration.Short
) : SnackbarVisuals


@Composable
fun LafyuuSnackBarHost(data: SnackbarData) {
    val visuals = data.visuals
    val type = (visuals as? LafyuuSnackBarVisuals)?.type ?: AlertType.Warning
    LafyuuSnackBar(message = visuals.message, type = type)
}


@Preview(showBackground = true, widthDp = 380, heightDp = 300)
@Composable
private fun LafyuuSnackBarPreview() {
    LafyuuTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            LafyuuSnackBar(message = "Saved successfully", type = AlertType.Success)
            LafyuuSnackBar(message = "An error occurred, try again", type = AlertType.Error)
            LafyuuSnackBar(message = "Your request is being processed", type = AlertType.Warning)
        }
    }
}