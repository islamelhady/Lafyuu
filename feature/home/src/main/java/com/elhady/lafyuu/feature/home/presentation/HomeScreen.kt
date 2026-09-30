package com.elhady.lafyuu.feature.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.elhady.lafyuu.core.designsystem.components.appbar.SearchTopBarWithNotification
import com.elhady.lafyuu.core.designsystem.components.element.LafyuuScaffold
import com.elhady.lafyuu.core.designsystem.components.element.NavigationBottomBar
import com.elhady.lafyuu.core.designsystem.components.element.defaultTabBarItems
import com.elhady.lafyuu.core.designsystem.components.text.LafyuuText
import com.elhady.lafyuu.core.designsystem.icons.Love
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTheme
import com.elhady.lafyuu.core.designsystem.theme.Theme
import com.elhady.lafyuu.core.designsystem.components.element.InfoStateContent
import com.elhady.lafyuu.feature.home.presentation.components.CategoryListSection
import com.elhady.lafyuu.feature.home.presentation.components.SaleProductSection
import com.elhady.lafyuu.feature.home.presentation.components.RecommendedProductsSection
import com.elhady.lafyuu.feature.home.presentation.components.SuperFlashSaleSection
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HomeRoute(
    onNavigateToProductDetails: (String) -> Unit = {},
    onNavigateToCategory: (categoryId: String, categoryName: String) -> Unit = { _, _ -> },
    onNavigateToFlashSale: () -> Unit = {},
    onNavigateToMegaSale: () -> Unit = {},
    onNavigateToCategories: () -> Unit = {},
    onNavigateToNotifications: () -> Unit = {},
    onNavigateToWishlist: () -> Unit = {},
    onNavigateToTab: (String) -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is HomeUiEffect.NavigateToProductDetails -> onNavigateToProductDetails(effect.productId)
                is HomeUiEffect.NavigateToCategoryProducts -> onNavigateToCategory(effect.categoryId, effect.categoryName)
                is HomeUiEffect.NavigateToFlashSale -> onNavigateToFlashSale()
                is HomeUiEffect.NavigateToMegaSale -> onNavigateToMegaSale()
                is HomeUiEffect.NavigateToCategoriesList -> onNavigateToCategories()
                is HomeUiEffect.NavigateToNotifications -> onNavigateToNotifications()
                is HomeUiEffect.NavigateToWishlist -> onNavigateToWishlist()
                is HomeUiEffect.NavigateToTab -> onNavigateToTab(effect.tabRoute)
                is HomeUiEffect.ShowSnackbar -> {}
            }
        }
    }

    HomeScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent
    )
}

@Composable
internal fun HomeScreen(
    uiState: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit
) {
    LafyuuScaffold(
        topBar = {
            SearchTopBarWithNotification(
                searchValue = uiState.searchQuery,
                onSearchValueChange = { onEvent(HomeUiEvent.SearchQueryChanged(it)) },
                onClearSearchClick = { onEvent(HomeUiEvent.ClearSearchClicked) },
                trailingIcon = Love,
                onLeadingClick = {},
                onTrailingClick = { onEvent(HomeUiEvent.WishlistClicked) },
                hasNotification = true,
                onNotificationClick = { onEvent(HomeUiEvent.NotificationClicked) }
            )
        },
        bottomBar = {
            NavigationBottomBar(
                tabs = defaultTabBarItems,
                selectedTab = uiState.selectedTab,
                onTabSelected = { onEvent(HomeUiEvent.BottomTabSelected(it)) }
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Theme.color.blue)
                    }
                }

                uiState.errorMessage != null && uiState.isContentEmpty -> {
                    InfoStateContent(
                        errorMessage = uiState.errorMessage,
                        onRetryClick = { onEvent(HomeUiEvent.RetryClicked) },
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                uiState.searchQuery.isNotBlank() -> {
                    when {
                        uiState.isSearching -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(color = Theme.color.blue)
                            }
                        }

                        uiState.searchResults.isNotEmpty() -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(vertical = Theme.space.large)
                                    .verticalScroll(rememberScrollState())
                            ) {
                                RecommendedProductsSection(
                                    products = uiState.searchResults,
                                    onProductClick = { onEvent(HomeUiEvent.ProductClicked(it)) }
                                )
                            }
                        }

                        else -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(Theme.space.large),
                                contentAlignment = Alignment.Center
                            ) {
                                LafyuuText(
                                    text = "No products found for \"${uiState.searchQuery}\"",
                                    style = Theme.typography.normalTextRegular,
                                    color = Theme.color.neutralGrey,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }

                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = Theme.space.large)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(Theme.space.large)
                    ) {
                        SuperFlashSaleSection(
                            offers = uiState.offers,
                            onOfferClick = { offerId -> onEvent(HomeUiEvent.OfferBannerClicked(offerId)) },
                            onSeeMoreClick = { onEvent(HomeUiEvent.SeeMoreFlashSaleClicked) }
                        )
                        CategoryListSection(
                            categories = uiState.categories,
                            onCategoryClick = { categoryId, categoryName ->
                                onEvent(HomeUiEvent.CategoryClicked(categoryId, categoryName))
                            },
                            onSeeMoreClick = { onEvent(HomeUiEvent.SeeMoreCategoryClicked) }
                        )
                        SaleProductSection(
                            title = "Flash Sale",
                            products = uiState.flashSaleProducts,
                            onProductClick = { onEvent(HomeUiEvent.ProductClicked(it)) },
                            onSeeMoreClick = { onEvent(HomeUiEvent.SeeMoreFlashSaleClicked) }
                        )

                        SaleProductSection(
                            title = "Mega Sale",
                            products = uiState.megaSaleProducts,
                            onProductClick = { onEvent(HomeUiEvent.ProductClicked(it)) },
                            onSeeMoreClick = { onEvent(HomeUiEvent.SeeMoreMegaSaleClicked) }
                        )

                        RecommendedProductsSection(
                            products = uiState.recommendedProducts,
                            onProductClick = { onEvent(HomeUiEvent.ProductClicked(it)) }
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    LafyuuTheme {
        HomeScreen(
            uiState = HomeUiState(),
            onEvent = {}
        )
    }
}
