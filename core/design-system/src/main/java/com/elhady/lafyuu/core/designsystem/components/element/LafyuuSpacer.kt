package com.elhady.lafyuu.core.designsystem.components.element

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.elhady.lafyuu.core.designsystem.theme.Theme

@Composable
fun VerticalSpacer(
    modifier: Modifier = Modifier,
    height: Dp = Theme.space.medium
) {
    Spacer(modifier = modifier.height(height))
}

@Composable
fun HorizontalSpacer(
    modifier: Modifier = Modifier,
    width: Dp = Theme.space.medium
) {
    Spacer(modifier = modifier.width(width))
}

@Composable
fun VerticalSpacerSmall(modifier: Modifier = Modifier) = VerticalSpacer(modifier = modifier, height = Theme.space.small)

@Composable
fun VerticalSpacerMedium(modifier: Modifier = Modifier) = VerticalSpacer(modifier = modifier, height = Theme.space.medium)

@Composable
fun VerticalSpacerLarge(modifier: Modifier = Modifier) = VerticalSpacer(modifier = modifier, height = Theme.space.large)

@Composable
fun VerticalSpacerExtraLarge(modifier: Modifier = Modifier) = VerticalSpacer(modifier = modifier, height = Theme.space.extraLarge)

@Composable
fun HorizontalSpacerSmall(modifier: Modifier = Modifier) = HorizontalSpacer(modifier = modifier, width = Theme.space.small)

@Composable
fun HorizontalSpacerMedium(modifier: Modifier = Modifier) = HorizontalSpacer(modifier = modifier, width = Theme.space.medium)

@Composable
fun HorizontalSpacerLarge(modifier: Modifier = Modifier) = HorizontalSpacer(modifier = modifier, width = Theme.space.large)
