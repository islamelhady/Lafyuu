package com.elhady.lafyuu.feature.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.elhady.lafyuu.core.designsystem.R
import com.elhady.lafyuu.core.designsystem.components.appbar.SearchTopBarWithNotification
import com.elhady.lafyuu.core.designsystem.components.card.Banner
import com.elhady.lafyuu.core.designsystem.components.element.LafyuuScaffold
import com.elhady.lafyuu.core.designsystem.components.element.NavigationBottomBar
import com.elhady.lafyuu.core.designsystem.components.element.defaultTabBarItems
import com.elhady.lafyuu.core.designsystem.icons.Love
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTheme
import com.elhady.lafyuu.core.designsystem.theme.Theme

@Composable
fun HomeRoute(
    onNavigateToProductDetails: (String) -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeScreen(
        uiState = uiState,
        onSearchQueryChange = viewModel::onSearchQueryChange,
        onClearSearch = viewModel::onClearSearch
    )
}

@Composable
internal fun HomeScreen(
    uiState: HomeUiState,
    onSearchQueryChange: (String) -> Unit,
    onClearSearch: () -> Unit
) {
    LafyuuScaffold(
        topBar = {
            SearchTopBarWithNotification(
                searchValue = uiState.searchQuery,
                onSearchValueChange = onSearchQueryChange,
                onClearSearchClick = onClearSearch,
                trailingIcon = Love,
                onLeadingClick = {},
                onTrailingClick = {},
                hasNotification = true,
                onNotificationClick = {}
            )
        },
        bottomBar = {
            NavigationBottomBar(
                tabs = defaultTabBarItems,
                onTabSelected = {}
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Theme.space.large)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(Theme.space.large)
        ) {
            Banner(
                title = "Super Flash Sale",
                subtitle = "50% Off",
                hours = "08",
                minutes = "34",
                seconds = "52",
                imageResId = R.drawable.img_promo_shoes_red
            )
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    LafyuuTheme {
        HomeScreen(
            uiState = HomeUiState(),
            onSearchQueryChange = {},
            onClearSearch = {}
        )
    }
}
