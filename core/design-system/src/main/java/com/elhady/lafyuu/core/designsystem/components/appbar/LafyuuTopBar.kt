package com.elhady.lafyuu.core.designsystem.components.appbar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.elhady.lafyuu.core.designsystem.theme.Theme


@Composable
internal fun LafyuuTopBar(
    modifier: Modifier = Modifier,
    leading: (@Composable () -> Unit)? = null,
    trailing: List<@Composable () -> Unit> = emptyList(),
    center: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(Theme.space.medium),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Theme.space.small)
    ) {
        leading?.invoke()
        center()
        trailing.forEach { it() }
    }
}