package com.elhady.lafyuu.core.designsystem.components.other

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
fun CountdownTimer(
    hours: Int,
    minutes: Int,
    seconds: Int,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        TimeBox(hours)
        Separator()
        TimeBox(minutes)
        Separator()
        TimeBox(seconds)
    }
}

@Composable
private fun TimeBox(value: Int) {
    Box(
        modifier = Modifier
            .clip(Theme.corner.small)
            .background(color = Theme.color.backgroundWhite)
            .padding(horizontal = Theme.space.small, vertical = Theme.space.extraSmall),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = value.toString().padStart(2, '0'),
            style = Theme.typography.mediumTextBold,
            color = Theme.color.neutralDark
        )
    }
}

@Composable
private fun Separator() {
    Text(
        text = ":",
        style = Theme.typography.mediumTextBold,
        color = Theme.color.neutralDark,
        modifier = Modifier.padding(horizontal = Theme.space.extraExtraSmall)
    )
}

@Preview(showBackground = true)
@Composable
private fun AllOtherComponentsPreview() {
    LafyuuTheme {
        Surface {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {

                SectionTitle("Time")
                CountdownTimer(hours = 8, minutes = 34, seconds = 52)
            }
        }
    }
}