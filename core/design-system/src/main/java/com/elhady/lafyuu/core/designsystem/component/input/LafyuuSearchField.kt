package com.elhady.lafyuu.core.designsystem.component.input

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.elhady.lafyuu.core.designsystem.theme.LafyuuColor

@Composable
fun LafyuuSearchField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Search",
    enabled: Boolean = true,
    onClearClick: (() -> Unit)? = null
) {
    LafyuuTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        placeholder = placeholder,
        enabled = enabled,
        leadingContent = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = LafyuuColor.TextSecondary
            )
        },
        trailingContent = {
            if (value.isNotEmpty() && onClearClick != null) {
                IconButton(
                    onClick = onClearClick
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear"
                    )
                }
            }
        }
    )
}

@Preview
@Composable
fun LafyuuSearchFieldPreview() {
    LafyuuSearchField(
        value = "Hello",
        onValueChange = {}
    )
}