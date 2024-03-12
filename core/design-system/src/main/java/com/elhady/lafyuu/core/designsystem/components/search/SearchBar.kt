package com.elhady.lafyuu.core.designsystem.components.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.elhady.lafyuu.core.designsystem.components.appbar.IconClick
import com.elhady.lafyuu.core.designsystem.components.textfield.BaseTextField
import com.elhady.lafyuu.core.designsystem.icons.Search
import com.elhady.lafyuu.core.designsystem.icons.X
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTheme
import com.elhady.lafyuu.core.designsystem.theme.Theme

@Composable
fun SearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Search Product"
) {
    BaseTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = placeholder,
        modifier = modifier.padding(
            vertical = Theme.space.medium
        ),
        tint = Theme.color.blue,
        leadingIcon = Search,
        trailingContent = {
            if (value.isNotEmpty()) {
                IconClick(
                    icon = X,
                    onClick = { onClear() },
                    modifier = Modifier.padding(Theme.space.extraSmall)
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun SearchPreview() {
    LafyuuTheme {
        Column(
            modifier = Modifier.padding(Theme.space.large),
            verticalArrangement = Arrangement.spacedBy(Theme.space.small)
        ) {
            var search by remember { mutableStateOf("") }
            SearchBar(
                value = search,
                onValueChange = { search = it },
                onClear = { search = "" }
            )
            SearchBar(
                value = "Nike Air Max",
                onValueChange = { },
                onClear = { }
            )
        }
    }
}