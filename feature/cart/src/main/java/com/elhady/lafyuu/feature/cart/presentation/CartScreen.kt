package com.elhady.lafyuu.feature.cart.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.elhady.lafyuu.core.designsystem.components.appbar.SingleTopAppBar
import com.elhady.lafyuu.core.designsystem.components.button.DefaultButton
import com.elhady.lafyuu.core.designsystem.components.button.QuantityButton
import com.elhady.lafyuu.core.designsystem.components.card.WideProductCard
import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.core.designsystem.components.element.InfoStateContent
import com.elhady.lafyuu.core.designsystem.components.element.LafyuuScaffold
import com.elhady.lafyuu.core.designsystem.components.element.NavigationBottomBar
import com.elhady.lafyuu.core.designsystem.components.element.defaultTabBarItems
import com.elhady.lafyuu.core.designsystem.components.other.LafyuuSnackBarVisuals
import com.elhady.lafyuu.core.designsystem.theme.Theme
import com.elhady.lafyuu.core.ui.text.asString
import com.elhady.lafyuu.feature.cart.R
import com.elhady.lafyuu.feature.cart.presentation.components.CartSummarySection
import kotlinx.coroutines.launch

@Composable
fun CartRoute(
    onNavigateToCheckout: () -> Unit,
    onNavigateToProductDetails: (String) -> Unit,
    onNavigateToTab: (String) -> Unit,
    modifier: Modifier = Modifier,
    selectedTab: String = "cart",
    viewModel: CartViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(viewModel, lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.uiEffect.collect { effect ->
                when (effect) {
                    CartUiEffect.NavigateToCheckout -> onNavigateToCheckout()
                    is CartUiEffect.NavigateToProductDetails -> onNavigateToProductDetails(effect.productId)
                    is CartUiEffect.NavigateToTab -> onNavigateToTab(effect.tabRoute)
                    is CartUiEffect.ShowSnackbar -> {
                        snackbarHostState.currentSnackbarData?.dismiss()
                        // separate coroutine: showSnackbar suspends until dismissed
                        launch {
                            snackbarHostState.showSnackbar(
                                visuals = LafyuuSnackBarVisuals(
                                    message = effect.message.asString(context),
                                    type = effect.type
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    CartScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onIntent = viewModel::onIntent,
        modifier = modifier,
        selectedTab = selectedTab
    )
}

@Composable
fun CartScreen(
    uiState: CartUiState,
    snackbarHostState: SnackbarHostState,
    onIntent: (CartUiIntent) -> Unit,
    modifier: Modifier = Modifier,
    selectedTab: String = "cart"
) {
    LafyuuScaffold(
        snackbarHostState = snackbarHostState,
        topBar = {
            SingleTopAppBar(
                title = stringResource(R.string.cart_title),
                onLeadingClick = {}
            )
        },
        bottomBar = {
            NavigationBottomBar(
                tabs = defaultTabBarItems.map { tab ->
                    if (tab.route == "cart") {
                        tab.copy(badgeCount = uiState.totalQuantity.takeIf { it > 0 })
                    } else {
                        tab
                    }
                },
                selectedTab = selectedTab,
                onTabSelected = { onIntent(CartUiIntent.BottomTabSelected(it)) }
            )
        }
    ) {
        val cart = uiState.cart
        when {
            uiState.isLoading && uiState.items.isEmpty() -> CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Theme.color.blue
            )

            uiState.error != null && uiState.items.isEmpty() -> InfoStateContent(
                errorMessage = uiState.error.asString(),
                onRetryClick = { onIntent(CartUiIntent.RetryClicked) },
                errorType = AlertType.Error,
                modifier = Modifier.align(Alignment.Center)
            )

            cart == null || cart.items.isEmpty() -> InfoStateContent(
                errorMessage = stringResource(R.string.cart_empty),
                errorType = AlertType.Success,
                modifier = Modifier.align(Alignment.Center)
            )

            else -> Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = Theme.space.large)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(Theme.space.medium)
                ) {
                    items(cart.items, key = { it.itemId }) { item ->
                        val isUpdating = item.itemId in uiState.updatingItemIds
                        WideProductCard(
                            imageUrl = item.imageUrl,
                            name = item.name,
                            price = item.price,
                            isFavorite = item.productId in uiState.favoriteProductIds,
                            onFavoriteClick = { onIntent(CartUiIntent.FavoriteClicked(item.productId)) },
                            onDeleteClick = { onIntent(CartUiIntent.RemoveItem(item.itemId)) },
                            onClick = { onIntent(CartUiIntent.ProductClicked(item.productId)) },
                            quantityButton = {
                                if (isUpdating) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.padding(Theme.space.small),
                                        color = Theme.color.blue
                                    )
                                } else {
                                    QuantityButton(
                                        quantity = item.quantity,
                                        onDecrement = {
                                            onIntent(CartUiIntent.DecreaseQuantity(item.itemId))
                                        },
                                        onIncrement = {
                                            onIntent(CartUiIntent.IncreaseQuantity(item.itemId))
                                        }
                                    )
                                }
                            }
                        )
                    }
                    item {
                        CartSummarySection(
                            summary = cart.summary,
                            couponCodeInput = uiState.couponCodeInput,
                            couponError = uiState.couponError,
                            onIntent = onIntent
                        )
                    }
                }
                DefaultButton(
                    caption = stringResource(R.string.cart_checkout),
                    isLoading = false,
                    onClick = { onIntent(CartUiIntent.CheckoutClicked) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}