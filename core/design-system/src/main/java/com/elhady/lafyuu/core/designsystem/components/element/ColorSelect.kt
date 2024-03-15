package com.elhady.lafyuu.core.designsystem.components.element

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.elhady.lafyuu.core.designsystem.components.text.SectionTitle
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTheme
import com.elhady.lafyuu.core.designsystem.theme.Theme

@Composable
fun ColorSelectRow(
    colors: List<Color>,
    selectedColor: Color,
    onColorSelected: (Color) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(Theme.space.large)
    ) {
        items(colors) { color ->
            val isSelected = color == selectedColor
            Box(
                modifier = Modifier
                    .size(Theme.size.large)
                    .clip(CircleShape)
                    .background(color)
                    .clickable { onColorSelected(color) },
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .size(Theme.size.mini)
                            .clip(CircleShape)
                            .background(Theme.color.backgroundWhite)
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true, widthDp = 390, heightDp = 200)
@Composable
private fun AllElementComponentsPreview() {
    LafyuuTheme {
        Surface {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {

                SectionTitle("Color Select")
                var selectedColor by remember { mutableStateOf(Color(0xFFFFC833)) }
                ColorSelectRow(
                    colors = listOf(
                        Color(0xFFFFC833), Color(0xFF40BFFF), Color(0xFFFB7181),
                        Color(0xFF53D1B6), Color(0xFF5C61F4), Color(0xFF223263),Color(0xFF5C51F4),
                    ),
                    selectedColor = selectedColor,
                    onColorSelected = { selectedColor = it }
                )
            }
        }
    }
}