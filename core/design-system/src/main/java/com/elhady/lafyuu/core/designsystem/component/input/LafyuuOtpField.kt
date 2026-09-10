package com.elhady.lafyuu.core.designsystem.component.input

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.elhady.lafyuu.core.designsystem.theme.LafyuuColor
import com.elhady.lafyuu.core.designsystem.theme.LafyuuDimens
import com.elhady.lafyuu.core.designsystem.theme.LafyuuShapes

@Composable
fun LafyuuOtpField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    length: Int = 4
) {
    BasicTextField(
        value = value,
        onValueChange = { newValue ->
            if (
                newValue.length <= length &&
                newValue.all(Char::isDigit)
            ) {
                onValueChange(newValue)
            }
        },
        modifier = modifier,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        ),
        decorationBox = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(
                    LafyuuDimens.Space12
                )
            ) {
                repeat(length) { index ->

                    val digit = value
                        .getOrNull(index)
                        ?.toString()
                        .orEmpty()

                    Box(
                        modifier = Modifier.size(LafyuuDimens.OtpBoxSize)
                            .background(
                                color = LafyuuColor.Surface,
                                shape = LafyuuShapes.Medium
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = digit,
                            style = TextStyle(
                                fontSize = 20.sp,
                                textAlign = TextAlign.Center
                            ),
                            color = LafyuuColor.TextPrimary
                        )
                    }
                }
            }
        }
    )
}

@Preview
@Composable
fun LafyuuOtpFieldPreview() {
    LafyuuOtpField(
        value = "1234",
        onValueChange = {}
    )
}