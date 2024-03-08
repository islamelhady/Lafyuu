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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.elhady.lafyuu.core.designsystem.components.text.SectionTitle
import com.elhady.lafyuu.core.designsystem.icons.Message
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTheme

@Composable
fun EmailTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Your Email",
    state: LafyuuFieldState = LafyuuFieldState.Default,
    errorMessage: String? = "Oops! Your Email Is Not Correct"
) {
    BaseTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = placeholder,
        state = state,
        errorMessage = errorMessage,
        leadingIcon = Message,
        keyboardType = KeyboardType.Email,
        modifier = modifier
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

                SectionTitle("Email")
                var emailPlaceholder by remember { mutableStateOf("") }
                EmailTextField(
                    value = emailPlaceholder,
                    onValueChange = { emailPlaceholder = it },
                    state = LafyuuFieldState.Default
                )
                var emailFilled by remember { mutableStateOf("islam.elhadyy@gmail.com") }
                EmailTextField(
                    value = emailFilled,
                    onValueChange = { emailFilled = it },
                    state = LafyuuFieldState.Default
                )
                var emailActive by remember { mutableStateOf("islam.elhadyy@gmail.com") }
                EmailTextField(
                    value = emailActive,
                    onValueChange = { emailActive = it },
                    state = LafyuuFieldState.Active
                )
                var emailError by remember { mutableStateOf("islam.elhadyy@gmail.com") }
                EmailTextField(
                    value = emailError,
                    onValueChange = { emailError = it },
                    state = LafyuuFieldState.Error
                )
            }
        }
    }
}