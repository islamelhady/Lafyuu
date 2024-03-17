package com.elhady.lafyuu.core.designsystem.components.textfield

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.unit.dp
import com.elhady.lafyuu.core.designsystem.components.text.SectionTitle
import com.elhady.lafyuu.core.designsystem.icons.User
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTheme

@Composable
fun DefaultTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Placeholder",
    state: LafyuuFieldState = LafyuuFieldState.Default,
    errorMessage: String? = null
) {
    LafyuuTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = placeholder,
        state = state,
        errorMessage = errorMessage,
        modifier = modifier,
        leadingIcon = User,
    )
}

@Preview(showBackground = true, widthDp = 380, heightDp = 800)
@Composable
private fun AllFormFieldsPreview() {
    LafyuuTheme {
        Surface {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {

                SectionTitle("Default")
                var defaultValue by remember { mutableStateOf("") }
                DefaultTextField(
                    value = defaultValue,
                    onValueChange = { defaultValue = it },
                    placeholder = "Placeholder",
                    state = LafyuuFieldState.Default
                )
                var filledValue by remember { mutableStateOf("Fill Form") }
                DefaultTextField(
                    value = filledValue,
                    onValueChange = { filledValue = it },
                    state = LafyuuFieldState.Default
                )
                var activeValue by remember { mutableStateOf("Active") }
                DefaultTextField(
                    value = activeValue,
                    onValueChange = { activeValue = it },
                    state = LafyuuFieldState.Active
                )
                var errorValue by remember { mutableStateOf("Active") }
                DefaultTextField(
                    value = errorValue,
                    onValueChange = { errorValue = it },
                    state = LafyuuFieldState.Error,
                    errorMessage = "Oops! This field is not valid"
                )
            }
        }
    }
}