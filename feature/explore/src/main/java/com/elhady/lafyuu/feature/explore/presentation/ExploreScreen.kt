package com.elhady.lafyuu.feature.explore.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.elhady.lafyuu.core.designsystem.components.appbar.SearchTopBarWithNotification
import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.core.designsystem.components.element.InfoStateContent
import com.elhady.lafyuu.core.designsystem.components.element.LafyuuScaffold
import com.elhady.lafyuu.core.designsystem.components.element.NavigationBottomBar
import com.elhady.lafyuu.core.designsystem.components.element.defaultTabBarItems
import com.elhady.lafyuu.core.designsystem.components.other.LafyuuSnackBarVisuals
import com.elhady.lafyuu.core.designsystem.components.other.ProductCategory
import com.elhady.lafyuu.core.designsystem.components.text.SectionTitle
import com.elhady.lafyuu.core.designsystem.icons.Bikini
import com.elhady.lafyuu.core.designsystem.icons.Dress
import com.elhady.lafyuu.core.designsystem.icons.Love
import com.elhady.lafyuu.core.designsystem.icons.ManShoes
import com.elhady.lafyuu.core.designsystem.icons.ManUnderwear
import com.elhady.lafyuu.core.designsystem.icons.Shirt
import com.elhady.lafyuu.core.designsystem.icons.Skirt
import com.elhady.lafyuu.core.designsystem.icons.Tshirt
import com.elhady.lafyuu.core.designsystem.icons.WomanBag
import com.elhady.lafyuu.core.designsystem.icons.WomanPants
import com.elhady.lafyuu.core.designsystem.icons.WomanShoes
import com.elhady.lafyuu.core.designsystem.icons.WomanTshirt
import com.elhady.lafyuu.core.designsystem.theme.Theme

@Composable
fun ExploreRoute(
    onNavigateToCategoryProducts: (String, String) -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigateToWishlist: () -> Unit,
    onNavigateToTab: (String) -> Unit,
    viewModel: ExploreViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is ExploreUiEffect.NavigateToCategoryProducts -> onNavigateToCategoryProducts(
                    effect.categoryId,
                    effect.categoryName
                )

                ExploreUiEffect.NavigateToSearch -> onNavigateToSearch()
                is ExploreUiEffect.NavigateToNotifications -> onNavigateToNotifications()
                is ExploreUiEffect.NavigateToWishlist -> onNavigateToWishlist()
                is ExploreUiEffect.NavigateToTab -> onNavigateToTab(effect.tabRoute)
                is ExploreUiEffect.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        visuals = LafyuuSnackBarVisuals(
                            message = effect.message,
                            type = effect.type
                        )
                    )
                }
            }
        }
    }

    ExploreScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onEvent = viewModel::onEvent,
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ExploreScreen(
    uiState: ExploreUiState,
    snackbarHostState: SnackbarHostState,
    onEvent: (ExploreUiEvent) -> Unit,
) {
    LafyuuScaffold(
        snackbarHostState = snackbarHostState,
        topBar = {
            SearchTopBarWithNotification(
//                searchValue = "",
//                onSearchValueChange = { onEvent(ExploreUiEvent.SearchClicked) },
                onSearchClick = { onEvent(ExploreUiEvent.SearchClicked) },
                trailingIcon = Love,
                onLeadingClick = {},
                onTrailingClick = { onEvent(ExploreUiEvent.WishlistClicked) },
                hasNotification = true,
                onNotificationClick = { onEvent(ExploreUiEvent.NotificationClicked) }
            )
        },
        bottomBar = {
            NavigationBottomBar(
                tabs = defaultTabBarItems,
                selectedTab = "explore",
                onTabSelected = { onEvent(ExploreUiEvent.BottomTabSelected(it)) }
            )
        }
    ) {
        when {
            uiState.isLoading && uiState.groups.isEmpty() -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Theme.color.blue
                )
            }

            uiState.groups.isEmpty() -> {
                InfoStateContent(
                    errorMessage = "No categories available",
                    onRetryClick = { onEvent(ExploreUiEvent.RetryClicked) },
                    errorType = AlertType.Success,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = Theme.space.large),
                    verticalArrangement = Arrangement.spacedBy(Theme.space.large)
                ) {
                    items(uiState.groups, key = { it.group.name }) { groupModel ->
                        Column(
                            verticalArrangement = Arrangement.spacedBy(Theme.space.medium)
                        ) {
                            SectionTitle(groupModel.group.title)
                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(Theme.space.medium),
                                verticalArrangement = Arrangement.spacedBy(Theme.space.medium)
                            ) {
                                groupModel.categories.forEach { category ->
                                    ProductCategory(
                                        label = category.name.replaceFirstChar { it.uppercase() },
                                        icon = getCategoryIcon(categoryName = category.name),
                                        onClick = {
                                            onEvent(
                                                ExploreUiEvent.CategoryClicked(
                                                    categoryId = category.id,
                                                    categoryName = category.name
                                                )
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun getCategoryIcon(categoryName: String): ImageVector {
    val name = categoryName.lowercase()
    return when {
        name.contains("charms") -> Tshirt
        name.contains("anklets") -> Shirt
        name.contains("pendants") -> Dress
        name.contains("earrings") -> Bikini
        name.contains("clothes") -> WomanBag
        name.contains("bags") -> WomanBag
        name.contains("jewelry") -> ManShoes
        name.contains("necklaces") -> ManUnderwear
        name.contains("bracelets") -> Bikini
        name.contains("rings") -> Skirt
        name.contains("glasses") -> WomanPants
        name.contains("sneakers") -> WomanShoes
        name.contains("watches") -> WomanTshirt
        name.contains("brooches") -> WomanTshirt
        name.contains("bangles") -> WomanTshirt
        name.contains("jewelry-sets") -> WomanTshirt
        else -> Shirt
    }
}
