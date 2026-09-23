package com.elhady.lafyuu.core.designsystem.components.textfield

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.elhady.lafyuu.core.designsystem.components.text.LafyuuText
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTheme
import com.elhady.lafyuu.core.designsystem.theme.Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectField(
    selectedOption: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Egypt",
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier
    ) {
        LafyuuTextField(
            value = selectedOption.ifEmpty { label },
            onValueChange = {},
            placeholder = label,
            readOnly = true,
            trailingContent = {
                ExposedDropdownMenuDefaults
                    .TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
                .height(Theme.size.inputFieldHeight),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .padding(top = Theme.space.small)
                .background(Theme.color.backgroundWhite)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        LafyuuText(
                            text = option,
                            style = if (option == selectedOption) Theme.typography.mediumTextBold else Theme.typography.mediumTextRegular,
                            color = if (option == selectedOption) Theme.color.blue else Theme.color.neutralGrey
                        )
                    },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}


@Preview(showBackground = true, widthDp = 380, heightDp = 400)
@Composable
fun SelectionFieldsPreview() {
    LafyuuTheme {
        Column(
            modifier = Modifier.padding(Theme.space.large),
            verticalArrangement = Arrangement.spacedBy(Theme.space.small)
        ) {
            var selectedOption by remember { mutableStateOf("Egypt") }
            SelectField(
                selectedOption = "United States",
                options = listOf("Egypt", "United States", "Saudi Arabia"),
                onOptionSelected = {})

            SelectField(
                selectedOption = selectedOption,
                options = listOf("Egypt", "United States", "Saudi Arabia"),
                onOptionSelected = { selectedOption = it }
            )
        }
    }
}



