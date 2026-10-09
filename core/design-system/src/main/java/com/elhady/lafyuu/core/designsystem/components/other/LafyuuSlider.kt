package com.elhady.lafyuu.core.designsystem.components.other

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.elhady.lafyuu.core.designsystem.components.text.LafyuuText
import com.elhady.lafyuu.core.designsystem.components.text.SectionTitle
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTheme
import com.elhady.lafyuu.core.designsystem.theme.Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LafyuuSlider(
    value: ClosedFloatingPointRange<Float>,
    onValueChange: (ClosedFloatingPointRange<Float>) -> Unit,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
) {
    val inactiveColor = Theme.color.neutralLight
    val activeColor = Theme.color.blue

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Theme.space.extraSmall)
    ) {
        RangeSlider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            modifier = Modifier.fillMaxWidth(),
            startThumb = {
                LafyuuSliderThumb()
            },
            endThumb = {
                LafyuuSliderThumb()
            },
            track = { _ ->
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                ) {
                    val trackY = size.height / 2f
                    val span = valueRange.endInclusive - valueRange.start
                    val startFraction = if (span > 0f) (value.start - valueRange.start) / span else 0f
                    val endFraction = if (span > 0f) (value.endInclusive - valueRange.start) / span else 1f

                    val startX = startFraction.coerceIn(0f, 1f) * size.width
                    val endX = endFraction.coerceIn(0f, 1f) * size.width

                    drawLine(
                        color = inactiveColor,
                        start = Offset(0f, trackY),
                        end = Offset(size.width, trackY),
                        strokeWidth = size.height,
                        cap = StrokeCap.Round,
                    )

                    drawLine(
                        color = activeColor,
                        start = Offset(startX, trackY),
                        end = Offset(endX, trackY),
                        strokeWidth = size.height,
                        cap = StrokeCap.Round,
                    )
                }
            }
        )

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = Theme.space.medium),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            LafyuuText(
                text = "MIN",
                style = Theme.typography.largeCaptionBold,
                color = Theme.color.neutralGrey
            )
            LafyuuText(
                text = "MAX",
                style = Theme.typography.largeCaptionBold,
                color = Theme.color.neutralGrey
            )
        }
    }
}

@Composable
private fun LafyuuSliderThumb(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.size(32.dp),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .background(
                    color = Theme.color.blue.copy(alpha = 0.2f),
                    shape = CircleShape,
                )
        )

        Box(
            modifier = Modifier
                .size(20.dp)
                .background(
                    color = Theme.color.blue,
                    shape = CircleShape,
                )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AllOtherComponentsParameterPreview() {
    LafyuuTheme {
        Surface {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {

                SectionTitle("Slider")
                var sliderValue: ClosedFloatingPointRange<Float> by remember { mutableStateOf(200f..800f) }
                LafyuuSlider(
                    value = sliderValue,
                    onValueChange = { sliderValue = it },
                    valueRange = 0f..2000f
                )
            }
        }
    }
}
