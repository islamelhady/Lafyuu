package com.elhady.lafyuu.core.designsystem.components.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
            horizontal = Theme.space.large,
            vertical = Theme.space.medium
        ),
        leadingIcon = Search,
        trailingContent = {
            if (value.isNotEmpty()) {
                Icon(
                    imageVector = X,
                    contentDescription = "Clear",
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { onClear() }
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