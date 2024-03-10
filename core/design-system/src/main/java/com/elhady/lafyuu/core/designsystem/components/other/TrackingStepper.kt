package com.elhady.lafyuu.core.designsystem.components.other

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.elhady.lafyuu.core.designsystem.components.text.SectionTitle
import com.elhady.lafyuu.core.designsystem.icons.Check
import com.elhady.lafyuu.core.designsystem.lafyuu.Size
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTheme
import com.elhady.lafyuu.core.designsystem.theme.Theme

@Composable
fun TrackingStepper(
    steps: List<String>,
    currentStep: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // Circles + Lines
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            steps.forEachIndexed { index, _ ->

                Box(
                    modifier = Modifier
                        .size(Theme.size.small)
                        .clip(CircleShape)
                        .background(
                            if (index <= currentStep) {
                                Theme.color.blue
                            } else {
                                Theme.color.neutralLight
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Check,
                        contentDescription = null,
                        tint = Theme.color.backgroundWhite,
                        modifier = Modifier.size(Theme.size.iconSmall)
                    )
                }

                if (index != steps.lastIndex) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(2.dp)
                            .background(
                                if (index < currentStep) {
                                    Theme.color.blue
                                } else {
                                    Theme.color.neutralLight
                                }
                            )
                    )
                }
            }
        }

        Spacer(
            modifier = Modifier.height(Theme.space.extraExtraSmall)
        )

        // Labels
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            steps.forEach { label ->
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        style = Theme.typography.mediumTextRegular
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SlideShowIndicatorPreview() {
    LafyuuTheme {
        Surface {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {

                SectionTitle("Tracking")
                TrackingStepper(
                    steps = listOf("Packing", "Shipping", "Arriving", "Success"),
                    currentStep = 1
                )
            }
        }
    }
}
