package com.elhady.lafyuu.core.designsystem.components.textfield

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import com.elhady.lafyuu.core.designsystem.components.text.HintText
import com.elhady.lafyuu.core.designsystem.components.text.LafyuuText
import com.elhady.lafyuu.core.designsystem.theme.Theme

enum class LafyuuFieldState { Default, Active, Error }

@Composable
internal fun LafyuuTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    state: LafyuuFieldState = LafyuuFieldState.Default,
    errorMessage: String? = null,
    readOnly: Boolean = false,
    minLines: Int = 1,
    textAreaSize: Dp = Theme.size.inputFieldHeight,
    leadingContent: @Composable (() -> Unit)? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingContent: @Composable (() -> Unit)? = null,
    shape: Shape = Theme.corner.small,
) {
    val effectiveState = if (state == LafyuuFieldState.Default && !errorMessage.isNullOrBlank()) {
        LafyuuFieldState.Error
    } else {
        state
    }

    val unfocusedBorderColor = when (effectiveState) {
        LafyuuFieldState.Default -> Theme.color.neutralLight
        LafyuuFieldState.Active -> Theme.color.blue
        LafyuuFieldState.Error -> Theme.color.error
    }

    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                HintText(
                    text = placeholder,
                    style = Theme.typography.normalTextRegular
                )
            },
            leadingIcon = leadingContent,
            trailingIcon = trailingContent,
            minLines = minLines,
            singleLine = minLines == 1,
            readOnly = readOnly,
            isError = effectiveState == LafyuuFieldState.Error,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            visualTransformation = visualTransformation,
            shape = shape,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Theme.color.blue,
                unfocusedBorderColor = unfocusedBorderColor,
                errorBorderColor = Theme.color.error,
                cursorColor = Theme.color.blue,
                errorCursorColor = Theme.color.error,
                focusedLeadingIconColor = Theme.color.blue,
                unfocusedLeadingIconColor = Theme.color.neutralGrey,
                errorLeadingIconColor = Theme.color.error
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(textAreaSize),
            textStyle = Theme.typography.normalTextBold.copy(color = Theme.color.neutralGrey)
        )
        if (effectiveState == LafyuuFieldState.Error && !errorMessage.isNullOrBlank()) {
            Spacer(Modifier.height(Theme.space.extraExtraSmall))
            LafyuuText(
                text = errorMessage,
                style = Theme.typography.normalTextBold,
                color = Theme.color.error
            )
        }
    }
}

@Composable
internal fun LafyuuTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    state: LafyuuFieldState = LafyuuFieldState.Default,
    errorMessage: String? = null,
    leadingIcon: ImageVector? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingContent: @Composable (() -> Unit)? = null
) {
    LafyuuTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = placeholder,
        modifier = modifier,
        state = state,
        errorMessage = errorMessage,
        leadingContent = leadingIcon?.let { icon ->
            {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                )
            }
        },
        keyboardType = keyboardType,
        visualTransformation = visualTransformation,
        trailingContent = trailingContent
    )
}