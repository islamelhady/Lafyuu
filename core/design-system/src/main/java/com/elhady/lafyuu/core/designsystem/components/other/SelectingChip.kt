package com.elhady.lafyuu.core.designsystem.components.other

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.elhady.lafyuu.core.designsystem.components.text.LafyuuText
import com.elhady.lafyuu.core.designsystem.components.text.SectionTitle
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTheme
import com.elhady.lafyuu.core.designsystem.theme.Theme

@Composable
fun SelectingChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(Theme.size.large)
            .clip(shape = Theme.corner.fullRounded)
            .background(
                color = Theme.color.backgroundWhite
            )
            .border(
                width = Theme.size.border,
                color = if (isSelected) Theme.color.blue else Theme.color.neutralLight,
                shape = Theme.corner.fullRounded
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        LafyuuText(
            text = label,
            style = Theme.typography.heading5,
            color = Theme.color.neutralDark
        )
    }
}

@Composable
fun SelectingGroup(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        options.forEach { option ->
            SelectingChip(
                label = option,
                isSelected = option == selectedOption,
                onClick = { onOptionSelected(option) },
                modifier = Modifier.padding(end = Theme.space.small)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SelectingChipPreview() {
    LafyuuTheme {
        Surface {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {

                SectionTitle("Selecting")
                var selectedSize by remember { mutableStateOf("7") }
                SelectingGroup(
                    options = listOf("6", "6.5", "7", "7.5", "8", "8.5"),
                    selectedOption = selectedSize,
                    onOptionSelected = { selectedSize = it }
                )
            }
        }
    }
}