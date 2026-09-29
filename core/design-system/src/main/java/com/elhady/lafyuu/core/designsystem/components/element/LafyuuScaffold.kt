package com.elhady.lafyuu.core.designsystem.components.element

import Left
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.elhady.lafyuu.core.designsystem.components.appbar.ProductTopBar
import com.elhady.lafyuu.core.designsystem.components.other.LafyuuSnackBarHost
import com.elhady.lafyuu.core.designsystem.components.other.LafyuuSnackBarVisuals
import com.elhady.lafyuu.core.designsystem.icons.More
import com.elhady.lafyuu.core.designsystem.icons.Search
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTheme
import com.elhady.lafyuu.core.designsystem.theme.Theme
import kotlinx.coroutines.launch

@Composable
fun LafyuuScaffold(
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    Box(
        Modifier
            .background(Theme.color.backgroundWhite)
            .padding(
                paddingValues = WindowInsets.systemBars.asPaddingValues()
            )
    ) {
        Column(Modifier.fillMaxSize()) {
            topBar()
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                content()
            }
            bottomBar()
        }
        Box(
            modifier = Modifier
                .padding(horizontal = Theme.space.huge)
                .padding(top = Theme.space.large)
        ) {
            SnackbarHost(hostState = snackbarHostState) { data ->
                LafyuuSnackBarHost(data = data)
            }
        }
    }

}

@Preview
@Composable
private fun LafyuuScaffoldPreview() {
    val tabs: List<TabBarItem> by remember {
        mutableStateOf(
            defaultTabBarItems
        )
    }
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    LafyuuTheme() {
        LafyuuScaffold(
            snackbarHostState = snackBarHostState,
            topBar = {
                ProductTopBar(
                    title = "Nike Air Max 270 Rea",
                    onLeadingClick = {},
                    leadingIcon = Left,
                    trailingIcon = More,
                    onTrailingClick = {},
                    searchIcon = Search,
                    onSearchClick = {},
                    contentDescription = null,
                )
            },
            bottomBar = {
                NavigationBottomBar(
                    tabs = tabs,
                    onTabSelected = {
                        scope.launch {
                            snackBarHostState.showSnackbar(
                                visuals = LafyuuSnackBarVisuals(
                                    message = it.label,
                                    type = when (it.label) {
                                        "Cart" -> AlertType.Success
                                        "Explore" -> AlertType.Error
                                        else -> AlertType.Warning
                                    }
                                )
                            )
                        }
                    }
                )
            },
            content = {},
        )
    }
}