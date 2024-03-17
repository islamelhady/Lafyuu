package com.elhady.lafyuu.core.designsystem.components.textfield

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.elhady.lafyuu.core.designsystem.components.text.LafyuuText
import com.elhady.lafyuu.core.designsystem.icons.Phone
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTheme
import com.elhady.lafyuu.core.designsystem.theme.Theme

@Composable
fun PhoneNumberTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    countryCode: String? = "+20",
    placeholder: String = "Phone Number",
    state: LafyuuFieldState = LafyuuFieldState.Default,
    errorMessage: String? = null
) {
    LafyuuTextField(
        value = value,
        onValueChange = { input ->
            val digitsOnly = input.filter { it.isDigit() }.take(10)
            onValueChange(digitsOnly)
        },
        placeholder = placeholder,
        state = state,
        errorMessage = errorMessage,
        keyboardType = KeyboardType.Phone,
        visualTransformation = PhoneNumberVisualTransformation(),
        leadingContent = {
            Row(
                modifier = Modifier.padding(start = Theme.space.medium),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Phone,
                    contentDescription = null
                )
                countryCode?.let {
                    LafyuuText(
                        countryCode,
                        style = Theme.typography.mediumTextRegular,
                        color = Theme.color.neutralGrey
                    )
                }
            }
        },
        modifier = modifier
    )
}


@Preview(showBackground = true)
@Composable
private fun PhoneNumberPreview() {
    LafyuuTheme {
        Surface {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                LafyuuText("Phone Number")
                var phone by remember { mutableStateOf("") }
                PhoneNumberTextField(
                    value = phone,
                    onValueChange = { phone = it },
                )
                var phoneFilled by remember { mutableStateOf("1141148538") }
                PhoneNumberTextField(
                    value = phoneFilled,
                    onValueChange = { phoneFilled = it },
                    state = LafyuuFieldState.Active
                )
            }
        }
    }
}