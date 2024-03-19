package com.elhady.lafyuu.core.designsystem.components.button

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.elhady.lafyuu.core.designsystem.icons.Facebook
import com.elhady.lafyuu.core.designsystem.icons.Google
import com.elhady.lafyuu.core.designsystem.theme.Theme

@Composable
fun SocialButton(
    icon: ImageVector,
    caption: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    isLoading: Boolean = false,
    loading: (@Composable () -> Unit)? = null,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(Theme.size.buttonHeight)
            .clip(Theme.corner.small)
            .background(Color.Transparent)
            .border(
                width = Theme.size.border,
                color = Theme.color.neutralLight,
                shape = Theme.corner.small
            )
            .clickable(enabled = !isLoading) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            loading?.invoke()
        } else {
            Image(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.align(Alignment.CenterStart)
                    .padding(start = Theme.space.extraLarge)
            )
            BasicText(
                text = caption,
                style = Theme.typography.mediumTextBold.copy(color = Theme.color.neutralGrey)
            )
        }
    }
}

@Preview
@Composable
fun SocialButtonGooglePreview() {
    SocialButton(
        onClick = {},
        caption = "Login with Google",
        icon = Google
    )
}

@Preview
@Composable
fun SocialButtonFacebookPreview() {
    SocialButton(
        onClick = {},
        caption = "Login with facebook",
        icon = Facebook
    )
}