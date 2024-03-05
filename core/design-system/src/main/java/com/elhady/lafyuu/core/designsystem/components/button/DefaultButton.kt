package com.elhady.lafyuu.core.designsystem.components.button

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTheme

@Composable
fun DefaultButton(
    caption: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    isEnabled: Boolean = true,
){
    BaseButton(
        modifier = modifier,
        onClick = onClick,
        caption = caption,
        isLoading = isLoading,
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
        )
    }
}





