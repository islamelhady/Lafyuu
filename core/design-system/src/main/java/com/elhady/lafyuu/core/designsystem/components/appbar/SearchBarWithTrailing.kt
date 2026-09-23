package com.elhady.lafyuu.core.designsystem.components.appbar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.elhady.lafyuu.core.designsystem.components.search.SearchBar
import com.elhady.lafyuu.core.designsystem.icons.Filter
import com.elhady.lafyuu.core.designsystem.icons.Mic
import com.elhady.lafyuu.core.designsystem.icons.Short
import com.elhady.lafyuu.core.designsystem.theme.Theme

@Composable
fun SearchBarWithTrailing(
    searchValue: String,
    onSearchValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    onLeadingClick: () -> Unit = {},
    onClearSearchClick: () -> Unit,
    trailingIcon: ImageVector? = null,
    onTrailingClick: () -> Unit,
    filterIcon: ImageVector? = null,
    onFilterClick: () -> Unit = {},
) {
    LafyuuTopBar(
        modifier = modifier,
        leading = {
            leadingIcon?.let {
                IconClick(
                    icon = it,
                    onClick = onLeadingClick
                )
            }
        },
        trailing = listOf(
            {
                trailingIcon?.let {
                    IconClick(
                        icon = it,
                        onClick = onTrailingClick,
                    )
                }
                filterIcon?.let {
                    IconClick(
                        icon = it,
                        onClick = onFilterClick,
                        tint = Theme.color.blue
                    )
                }
            }
        )
    )
    {
        SearchBar(
            value = searchValue,
            onValueChange = onSearchValueChange,
            onClear = onClearSearchClick,
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchBarWithTrailingPreview() {
    var search by remember { mutableStateOf("") }
    Column(
        Modifier.padding(Theme.space.large)
    ) {
        SearchBarWithTrailing(
            searchValue = search,
            onSearchValueChange = { search = it },
            onClearSearchClick = { search = "" },
            trailingIcon = Short,
            onLeadingClick = {},
            onTrailingClick = {},
            filterIcon = Filter,
            onFilterClick = {},
        )

        SearchBarWithTrailing(
            searchValue = search,
            onSearchValueChange = { search = it },
            onClearSearchClick = { search = "" },
            trailingIcon = Mic,
            onTrailingClick = {},
        )
    }

}