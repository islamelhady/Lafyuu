package com.elhady.lafyuu.core.designsystem.components.textfield

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.elhady.lafyuu.core.designsystem.components.text.SectionTitle
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTheme

@Composable
fun TextAreaField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Write your review here",
    state: LafyuuFieldState = LafyuuFieldState.Default,
    textAreaSize: Dp = 160.dp,
    errorMessage: String? = null,
) {
    BaseTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .height(textAreaSize),
        placeholder = placeholder,
        state = state,
        errorMessage = errorMessage,
        minLines = 4,
        textAreaSize = textAreaSize
    )
}


@Preview(showBackground = true)
@Composable
private fun AllRemainingFormsPreview() {
    LafyuuTheme {
        Surface {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                SectionTitle("Text Area")
                var review by remember { mutableStateOf("") }
                TextAreaField(
                    value = review,
                    onValueChange = { review = it }
                )
                var reviewFilled by remember {
                    mutableStateOf("Ad velit voluptate laboris Ad velit voluptate laboris Ad velit voluptate laboris Ad velit voluptate laboris excepteur x. Ea tempor veniaillum anim fugiat pariatur qui mollit")
                }
                TextAreaField(
                    value = reviewFilled,
                    onValueChange = { reviewFilled = it }
                )
            }
        }
    }
}