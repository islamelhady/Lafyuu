package com.elhady.lafyuu.core.designsystem.component.appbar

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.tooling.preview.Preview
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTypography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LafyuuTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: ImageVector? = null,
    navigationContentDescription: String? = null,
    onNavigationClick: (() -> Unit)? = null,
    actionIcon: ImageVector? = null,
    actionContentDescription: String? = null,
    onActionClick: (() -> Unit)? = null,
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = title,
                style = LafyuuTypography.Title,
            )
        },
        navigationIcon = {
            if (
                (navigationIcon != null) &&
                (onNavigationClick != null)
            ) {
                IconButton(
                    onClick = onNavigationClick,
                ) {
                    Icon(
                        imageVector = navigationIcon,
                        contentDescription =
                            navigationContentDescription,
                    )
                }
            }
        },
        actions = {
            if (
                (actionIcon != null) &&
                (onActionClick != null)
            ) {
                IconButton(
                    onClick = onActionClick,
                ) {
                    Icon(
                        imageVector = actionIcon,
                        contentDescription =
                            actionContentDescription,
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
    )
}

@Preview
@Composable
fun LafyuuTopAppBarPreview(){
    LafyuuTopAppBar(title = "Lafyuu")
}