package com.elhady.lafyuu.core.designsystem.components.textfield

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.elhady.lafyuu.core.designsystem.components.text.LafyuuText
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTheme
import com.elhady.lafyuu.core.designsystem.theme.Theme

@Composable
fun OtpCodeTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    otpLength: Int = 6,
    isError: Boolean = false,
    errorMessage: String? = null
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            horizontalAlignment = Alignment.Start
        ) {
            BasicTextField(
                value = value,
                onValueChange = { input ->
                    val digitsOnly = input.filter { it.isDigit() }.take(otpLength)
                    onValueChange(digitsOnly)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                decorationBox = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(Theme.space.small),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        repeat(otpLength) { index ->
                            val char = value.getOrNull(index)?.toString() ?: ""
                            val isFocused =
                                value.length == index || (value.length == otpLength && index == otpLength - 1)

                            val borderColor = when {
                                isError -> Theme.color.error
                                isFocused -> Theme.color.blue
                                char.isNotEmpty() -> Theme.color.neutralGrey
                                else -> Theme.color.neutralLight
                            }

                            Box(
                                modifier = Modifier
                                    .size(Theme.size.large)
                                    .clip(Theme.corner.small)
                                    .background(Theme.color.backgroundWhite)
                                    .border(
                                        width = if (isFocused || isError) 1.5.dp else 1.dp,
                                        color = borderColor,
                                        shape = Theme.corner.small
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                LafyuuText(
                                    text = char,
                                    style = Theme.typography.heading4,
                                    color = Theme.color.neutralDark,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            )

            if (isError && errorMessage != null) {
                Spacer(Modifier.height(Theme.space.small))
                LafyuuText(
                    text = errorMessage,
                    style = Theme.typography.normalTextBold,
                    color = Theme.color.error
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OtpCodeTextFieldPreview() {
    LafyuuTheme {
        var otp by remember { mutableStateOf("") }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OtpCodeTextField(
                value = otp,
                onValueChange = { otp = it }
            )
            OtpCodeTextField(
                value = otp,
                onValueChange = { otp = it },
                isError = true,
                errorMessage = "Invalid OTP Code"
            )
        }
    }
}
