package com.elhady.lafyuu.core.designsystem.components.textfield

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class PhoneNumberVisualTransformation : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        val digits = text.text.filter { it.isDigit() }.take(10)

        val formatted = buildString {
            digits.forEachIndexed { index, char ->
                when (index) {
                    0 -> append("(")
                    3 -> append(") ")
                    6 -> append("-")
                }
                append(char)
            }
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                val digitCount = offset.coerceIn(0, digits.length)
                return when {
                    digitCount <= 0 -> 0
                    digitCount <= 3 -> digitCount + 1
                    digitCount <= 6 -> digitCount + 3
                    else -> digitCount + 4
                }
            }

            override fun transformedToOriginal(offset: Int): Int {
                for (digitCount in 0..digits.length) {
                    if (originalToTransformed(digitCount) >= offset) return digitCount
                }
                return digits.length
            }
        }
        return TransformedText(AnnotatedString(formatted), offsetMapping)
    }
}