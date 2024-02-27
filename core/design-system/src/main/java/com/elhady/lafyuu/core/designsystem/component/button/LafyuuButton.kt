package com.elhady.lafyuu.core.designsystem.component.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.elhady.lafyuu.core.designsystem.theme.LafyuuColor
import com.elhady.lafyuu.core.designsystem.theme.LafyuuDimens
import com.elhady.lafyuu.core.designsystem.theme.LafyuuShapes
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTypography

@Composable
fun LafyuuButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    leadingContent: (@Composable (() -> Unit))? = null,
    trailingContent: (@Composable (() -> Unit))? = null
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(LafyuuDimens.ButtonHeight),
        enabled = enabled && !loading,
        shape = LafyuuShapes.Medium,
        colors = ButtonDefaults.buttonColors(
            containerColor = LafyuuColor.Primary,
            contentColor = LafyuuColor.OnPrimary,
            disabledContainerColor = LafyuuColor.TextDisabled
        )
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.height(20.dp),
                color = LafyuuColor.OnPrimary,
                strokeWidth = 2.dp
            )
            return@Button
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            leadingContent?.let {
                it()
                Spacer(Modifier.width(8.dp))
            }

            Text(
                text = text,
                style = LafyuuTypography.Display
            )

            trailingContent?.let {
                Spacer(Modifier.width(8.dp))
                it()
            }
        }
    }
}

@Preview
@Composable
fun LafyuuButtonPreview() {
    LafyuuButton(
        text = "Login",
        onClick = {}
    )
}
