package com.elhady.lafyuu.core.designsystem.component.appbar

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTypography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LafyuuCenterTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    leadingContentDescription: String? = null,
    onLeadingClick: (() -> Unit)? = null,
    trailingIcon: ImageVector? = null,
    trailingContentDescription: String? = null,
    onTrailingClick: (() -> Unit)? = null,
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
                leadingIcon != null &&
                onLeadingClick != null
            ) {
                IconButton(
                    onClick = onLeadingClick
                ) {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription =
                            leadingContentDescription,
                    )
                }
            }
        },
        actions = {
            if (
                trailingIcon != null &&
                onTrailingClick != null
            ) {
                IconButton(
                    onClick = onTrailingClick
                ) {
                    Icon(
                        imageVector = trailingIcon,
                        contentDescription =
                            trailingContentDescription,
                    )
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
    )
}

@Preview
@Composable
fun LafyuuCenterTopAppBarPreview(){
    LafyuuCenterTopAppBar(
        title = "Lafyuu",
    )
}