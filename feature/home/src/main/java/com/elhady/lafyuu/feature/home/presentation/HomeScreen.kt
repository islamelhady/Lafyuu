package com.elhady.lafyuu.feature.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.elhady.lafyuu.core.designsystem.components.appbar.SearchTopBarWithNotification
import com.elhady.lafyuu.core.designsystem.components.element.InfoStateContent
import com.elhady.lafyuu.core.designsystem.components.element.LafyuuScaffold
import com.elhady.lafyuu.core.designsystem.components.element.NavigationBottomBar
import com.elhady.lafyuu.core.designsystem.components.element.defaultTabBarItems
import com.elhady.lafyuu.core.designsystem.icons.Love
import com.elhady.lafyuu.core.designsystem.theme.Theme
import com.elhady.lafyuu.core.ui.text.asString
import com.elhady.lafyuu.feature.home.R
import com.elhady.lafyuu.feature.home.presentation.components.CategoryListSection
import com.elhady.lafyuu.feature.home.presentation.components.OfferBannerSection
import com.elhady.lafyuu.feature.home.presentation.components.RecommendedProductsSection
import com.elhady.lafyuu.feature.home.presentation.components.SaleProductSection
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HomeRoute(
    onNavigateToSearch: () -> Unit = {},
    onNavigateToProductDetails: (String) -> Unit = {},
    onNavigateToCategoryProducts: (String, String) -> Unit = { _, _ -> },
    onNavigateToFlashSale: () -> Unit = {},
    onNavigateToMegaSale: () -> Unit = {},
    onNavigateToMoreCategories: () -> Unit = {},
    onNavigateToNotifications: () -> Unit = {},
    onNavigateToWishlist: () -> Unit = {},
    onNavigateToTab: (String) -> Unit = {},
    cartItemCount: Int = 0,
    selectedTab: String = "home",
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.effect.collect {
                viewModel.effect.collectLatest { effect ->
                    when (effect) {
                        is HomeUiEffect.NavigateToProductDetails -> onNavigateToProductDetails(
                            effect.productId
                        )

                        is HomeUiEffect.NavigateToCategoryProducts -> onNavigateToCategoryProducts(
                            effect.categoryId,
                            effect.categoryName
                        )

                        is HomeUiEffect.NavigateToFlashSale -> onNavigateToFlashSale()
                        is HomeUiEffect.NavigateToMegaSale -> onNavigateToMegaSale()
                        is HomeUiEffect.NavigateToMoreCategories -> onNavigateToMoreCategories()
                        is HomeUiEffect.NavigateToNotifications -> onNavigateToNotifications()
                        is HomeUiEffect.NavigateToFavourite -> onNavigateToWishlist()
                        is HomeUiEffect.NavigateToTab -> onNavigateToTab(effect.tabRoute)
                        HomeUiEffect.NavigateToSearch -> onNavigateToSearch()
                    }
                }
            }
        }
    }

    HomeScreen(
        uiState = uiState,
        onIntent = viewModel::onIntent,
        snackbarHostState = snackbarHostState,
        cartItemCount = cartItemCount,
        selectedTab = selectedTab
    )
}

@Composable
internal fun HomeScreen(
    uiState: HomeUiState,
    onIntent: (HomeUiIntent) -> Unit,
    snackbarHostState: SnackbarHostState,
    cartItemCount: Int = 0,
    selectedTab: String = "home"
) {
    LafyuuScaffold(
        snackbarHostState = snackbarHostState,
        topBar = {
            SearchTopBarWithNotification(
                onSearchClick = { onIntent(HomeUiIntent.SearchClicked) },
                trailingIcon = Love,
                onLeadingClick = {},
                onTrailingClick = { onIntent(HomeUiIntent.FavouriteClicked) },
                hasNotification = true,
                onNotificationClick = { onIntent(HomeUiIntent.NotificationClicked) }
            )
        },
        bottomBar = {
            NavigationBottomBar(
                tabs = defaultTabBarItems.map { tab ->
                    if (tab.route == "cart") {
                        tab.copy(badgeCount = if (cartItemCount > 0) cartItemCount else null)
                    } else {
                        tab
                    }
                },
                selectedTab = selectedTab,
                onTabSelected = { onIntent(HomeUiIntent.BottomTabSelected(it)) }
            )
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            when {
                uiState.isLoading -> CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Theme.color.blue
                )

                uiState.isContentEmpty -> InfoStateContent(
                    errorMessage = uiState.error?.asString() ?: stringResource(R.string.home_empty),
                    onRetryClick = { onIntent(HomeUiIntent.RetryClicked) },
                    modifier = Modifier.align(Alignment.Center)
                )

                else -> HomeContent(uiState = uiState, onIntent = onIntent)
            }
        }
    }
}

@Composable
private fun HomeContent(
    uiState: HomeUiState,
    onIntent: (HomeUiIntent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(vertical = Theme.space.large),
        verticalArrangement = Arrangement.spacedBy(Theme.space.large)
    ) {
        OfferBannerSection(offers = uiState.offers)

        CategoryListSection(
            categories = uiState.categories,
            onCategoryClick = { id, name -> onIntent(HomeUiIntent.CategoryClicked(id, name)) },
            onSeeMoreClick = { onIntent(HomeUiIntent.SeeMoreCategoryClicked) }
        )

        SaleProductSection(
            title = stringResource(R.string.home_flash_sale),
            products = uiState.flashSaleProducts,
            onProductClick = { onIntent(HomeUiIntent.ProductClicked(it)) },
            onSeeMoreClick = { onIntent(HomeUiIntent.SeeMoreFlashSaleClicked) }
        )

        SaleProductSection(
            title = stringResource(R.string.home_mega_sale),
            products = uiState.megaSaleProducts,
            onProductClick = { onIntent(HomeUiIntent.ProductClicked(it)) },
            onSeeMoreClick = { onIntent(HomeUiIntent.SeeMoreMegaSaleClicked) }
        )

        RecommendedProductsSection(
            products = uiState.recommendedProducts,
            onProductClick = { onIntent(HomeUiIntent.ProductClicked(it)) }
        )

        if (uiState.hasLoadProblem) {
            InfoStateContent(
                errorMessage = uiState.error?.asString() ?: stringResource(R.string.home_partial_error),
                onRetryClick = { onIntent(HomeUiIntent.RetryClicked) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}