package com.elhady.lafyuu.core.designsystem.component.appbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun LafyuuBackTopAppBar(
    title: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    actionIcon: ImageVector? = null,
    actionContentDescription: String? = null,
    onActionClick: (() -> Unit)? = null,
) {
    LafyuuTopAppBar(
        title = title,
        modifier = modifier,
        navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
        navigationContentDescription = "Back",
        onNavigationClick = onBackClick,
        actionIcon = actionIcon,
        actionContentDescription = actionContentDescription,
        onActionClick = onActionClick,
    )
}

@Preview
@Composable
fun LafyuuBackTopAppBarPreview() {
    LafyuuBackTopAppBar(
        title = "Lafyuu",
        onBackClick = {},
    )
}