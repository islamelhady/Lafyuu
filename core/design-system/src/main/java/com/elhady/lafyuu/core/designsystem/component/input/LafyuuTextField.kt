package com.elhady.lafyuu.core.designsystem.component.input

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.elhady.lafyuu.core.designsystem.theme.LafyuuColor
import com.elhady.lafyuu.core.designsystem.theme.LafyuuDimens
import com.elhady.lafyuu.core.designsystem.theme.LafyuuShapes

@Composable
fun LafyuuTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    isError: Boolean = false,
    supportingText: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    leadingContent: (@Composable (() -> Unit))? = null,
    trailingContent: (@Composable (() -> Unit))? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .height(LafyuuDimens.InputHeight),
        enabled = enabled,
        readOnly = readOnly,
        isError = isError,
        singleLine = true,
        shape = LafyuuShapes.Medium,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        label = label?.let {
            {
                Text(text = it)
            }
        },
        placeholder = placeholder?.let {
            {
                Text(text = it)
            }
        },
        supportingText = supportingText?.let {
            {
                Text(text = it)
            }
        },
        leadingIcon = leadingContent,
        trailingIcon = trailingContent,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = LafyuuColor.Primary,
            unfocusedBorderColor = LafyuuColor.Border,
            errorBorderColor = LafyuuColor.Error,
            focusedLabelColor = LafyuuColor.Primary,
            cursorColor = LafyuuColor.Primary
        )
    )
}

@Preview
@Composable
fun LafyuuTextFieldPreview() {
    LafyuuTextField(
        value = "Hello",
        onValueChange = {}
    )
}