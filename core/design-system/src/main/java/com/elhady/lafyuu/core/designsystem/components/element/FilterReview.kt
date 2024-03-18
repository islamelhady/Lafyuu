package com.elhady.lafyuu.core.designsystem.components.element

import StarFilled
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.elhady.lafyuu.core.designsystem.components.button.LabelButton
import com.elhady.lafyuu.core.designsystem.components.button.TextIconButton
import com.elhady.lafyuu.core.designsystem.components.text.SectionTitle
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTheme
import com.elhady.lafyuu.core.designsystem.theme.Theme

@Composable
fun FilterReviewBar(
    selectedStars: Int,
    onFilterSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val filters = listOf(0, 1, 2, 3, 4, 5)

    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(Theme.space.medium)
    ) {
        items(filters) { stars ->
            val isSelected = stars == selectedStars
            if (stars == 0) {
                LabelButton(
                    caption = "All Review",
                    modifier = Modifier,
                    onClick = { onFilterSelected(stars) },
                    containerColor = if (isSelected) Theme.color.blue.copy(alpha = 0.1f) else Color.Transparent,
                    contentColor = if (isSelected) Theme.color.blue else Theme.color.neutralGrey,
                    hasBorder = true,
                )
            } else {
                TextIconButton(
                    icon = StarFilled,
                    caption = stars.toString(),
                    onClick = { onFilterSelected(stars) },
                    containerColor = if (isSelected) Theme.color.blue.copy(alpha = 0.1f) else Color.Transparent,
                    contentColor = if (isSelected) Theme.color.blue else Theme.color.neutralGrey,
                    hasBorder = true,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AllElementComponentsPreview() {
    LafyuuTheme {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            var selectedStars by remember { mutableIntStateOf(0) }
            FilterReviewBar(
                selectedStars = selectedStars,
                onFilterSelected = { selectedStars = it }
            )
        }
    }
}