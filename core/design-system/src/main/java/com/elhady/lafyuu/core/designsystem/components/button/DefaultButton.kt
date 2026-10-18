package com.elhady.lafyuu.core.designsystem.components.button

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTheme
import com.elhady.lafyuu.core.designsystem.theme.Theme
import com.google.android.material.loadingindicator.LoadingIndicator

@Composable
fun DefaultButton(
    caption: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    isEnabled: Boolean = true,
){
    LafyuuButton(
        modifier = modifier,
        onClick = onClick,
        caption = caption,
        isLoading = isLoading,
        loading = {
            CircularProgressIndicator(
                modifier = Modifier.size(Theme.size.buttonHeight),
                color = Theme.color.backgroundWhite,
                strokeWidth = 2.dp
            )
        },
        isEnabled = isEnabled,
        hasShadow = true
    )
}


@Preview(name = "Default Button")
@Composable
private fun PreviewDefaultButton(){
    LafyuuTheme() {
        DefaultButton(
            modifier = Modifier.fillMaxWidth(),
            caption = "Default Button",
            onClick = {}
        )
    }
}

@Preview(name = "Default Button Disable")
@Composable
private fun PreviewDefaultButtonDisable(){
    LafyuuTheme {
        DefaultButton(
            modifier = Modifier.fillMaxWidth(),
            caption = "Default Button",
            onClick = {},
            isEnabled = false,
            isLoading = true
        )
    }
}





