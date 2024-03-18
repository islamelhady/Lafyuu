package com.elhady.lafyuu.core.designsystem.components.button

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.elhady.lafyuu.core.designsystem.components.text.LafyuuText
import com.elhady.lafyuu.core.designsystem.lafyuu.Size
import com.elhady.lafyuu.core.designsystem.theme.Theme

@Composable
internal fun BaseButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    icon: ImageVector? = null,
    iconSize: Dp = Theme.size.iconMedium,
    loading: (@Composable () -> Unit)? = null,
    caption: String? = null,
    isLoading: Boolean = false,
    isEnabled: Boolean = true,
    hasBorder: Boolean = false,
    paddingHorizontal: Dp = Theme.space.large,
    style: TextStyle = Theme.typography.mediumTextBold,
    borderColor: Color = Theme.color.neutralLight,
    containerColor: Color = Theme.color.blue,
    disableContainerColor: Color = Theme.color.backgroundWhite,
    contentColor: Color = Theme.color.backgroundWhite,
    disableContentColor: Color = Theme.color.neutralGrey,
    iconTint: Color? = contentColor,
    shape: Shape = Theme.corner.small,
    hasShadow: Boolean = false,
) {
    val backgroundColor = if (isEnabled) containerColor else disableContainerColor
    val textAndIconColor = if (isEnabled) contentColor else disableContentColor
    val shadowColor: Color = if (isEnabled) containerColor else disableContainerColor
    val finalIconTint = if (isEnabled) iconTint else disableContentColor
    val hasContentSpacing = icon != null && caption != null
    val outlineColor = if (!hasBorder) Color.Transparent else if (isEnabled) borderColor else Theme.color.backgroundWhite
    val isClickable = isEnabled && !isLoading

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .then(
                if (hasShadow) {
                    Modifier
                        .dropShadow(
                            shape = shape,
                            shadow = Shadow(
                                radius = 30.dp,
                                offset = DpOffset(x = 0.dp, y = 10.dp),
                                color = shadowColor.copy(alpha = 0.24f)
                            )
                        )
                } else Modifier
            )
            .height(Theme.size.buttonHeight)
            .clip(shape = shape)
            .background(backgroundColor)
            .border(width = 1.dp, color = outlineColor, shape = shape)
            .clickable(isClickable) {
                onClick()
            }
            .padding(horizontal = paddingHorizontal),

        horizontalArrangement = Arrangement.Center
    ) {
        icon?.let {
            Image(
                imageVector = it,
                contentDescription = "button icon",
                modifier = Modifier.size(iconSize),
                colorFilter = finalIconTint?.let { tint -> ColorFilter.tint(tint) }
            )
        }
        if (hasContentSpacing) {
            Spacer(Modifier.width(Theme.space.small))
        }
        if (isLoading) {
            loading?.invoke()
        } else {
            caption?.let {
                LafyuuText(
                    text = it,
                    style = style,
                    color = textAndIconColor
                )
            }
        }
    }
}