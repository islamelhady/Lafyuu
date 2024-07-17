package com.elhady.lafyuu.feature.cart.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.elhady.lafyuu.core.designsystem.components.appbar.SingleTopAppBar
import com.elhady.lafyuu.core.designsystem.components.button.QuantityButton
import com.elhady.lafyuu.core.designsystem.components.card.WideProductCard
import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.core.designsystem.components.element.InfoStateContent
import com.elhady.lafyuu.core.designsystem.components.element.LafyuuScaffold
import com.elhady.lafyuu.core.designsystem.components.element.NavigationBottomBar
import com.elhady.lafyuu.core.designsystem.components.element.defaultTabBarItems
import com.elhady.lafyuu.core.designsystem.components.other.LafyuuSnackBarVisuals
import com.elhady.lafyuu.core.designsystem.theme.Theme
import com.elhady.lafyuu.feature.cart.presentation.components.CartSummarySection

@Composable
fun CartRoute(
    onNavigateToCheckout: () -> Unit,
    onNavigateToProductDetails: (String) -> Unit,
    onNavigateToTab: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CartViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                CartUiEffect.NavigateToCheckout -> onNavigateToCheckout()
                is CartUiEffect.NavigateToProductDetails -> onNavigateToProductDetails(effect.productId)
                is CartUiEffect.NavigateToTab -> onNavigateToTab(effect.tabRoute)
                is CartUiEffect.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        visuals = LafyuuSnackBarVisuals(
                            message = effect.message,
                            type = AlertType.Success
                        )
                    )
                }
            }
        }
    }

    CartScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onEvent = viewModel::onEvent,
        modifier = modifier
    )
}

@Composable
fun CartScreen(
    uiState: CartUiState,
    snackbarHostState: SnackbarHostState,
    onEvent: (CartUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    LafyuuScaffold(
        snackbarHostState = snackbarHostState,
        topBar = {
            SingleTopAppBar(
                title = "Your Cart",
                onLeadingClick = {}
            )
        },
        bottomBar = {
            NavigationBottomBar(
                tabs = defaultTabBarItems.map { tab ->
                    if (tab.route == "cart") {
                        val totalQty = uiState.items.sumOf { it.quantity }
                        tab.copy(badgeCount = if (totalQty > 0) totalQty else null)
                    } else {
                        tab
                    }
                },
                selectedTab = "cart",
                onTabSelected = { onEvent(CartUiEvent.BottomTabSelected(it)) }
            )
        }
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
        ) {
            when {
                uiState.isLoading && uiState.items.isEmpty() -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Theme.color.blue
                    )
                }

                uiState.error != null && uiState.items.isEmpty() -> {
                    InfoStateContent(
                        errorMessage = uiState.error,
                        onRetryClick = { onEvent(CartUiEvent.RetryClicked) },
                        errorType = AlertType.Error,
                        modifier = Modifier.align(Alignment.Center),
                    )
                }

                uiState.items.isEmpty() -> {
                    InfoStateContent(
                        errorMessage = "Your Cart is Empty",
                        onRetryClick = { onEvent(CartUiEvent.LoadCart) },
                        errorType = AlertType.Success
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = Theme.space.large),
                        verticalArrangement = Arrangement.spacedBy(Theme.space.medium)
                    ) {
                        items(uiState.items, key = { it.itemId }) { item ->
                            val isUpdating = uiState.updatingItemIds.contains(item.itemId)
                            WideProductCard(
                                imageUrl = com.elhady.lafyuu.core.designsystem.R.drawable.img_product_shoes_blue,
                                name = item.productName,
                                price = String.format("%.2f", item.finalPricePerUnit),
                                isFavorite = false,
                                onFavoriteClick = {},
                                onDeleteClick = { onEvent(CartUiEvent.RemoveItem(item.itemId)) },
                                onClick = { onEvent(CartUiEvent.ProductClicked(item.productId)) },
                                quantityButton = {
                                    if (isUpdating) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.padding(8.dp),
                                            color = Theme.color.blue
                                        )
                                    } else {
                                        QuantityButton(
                                            quantity = item.quantity,
                                            onDecrement = {
                                                onEvent(
                                                    CartUiEvent.DecreaseQuantity(
                                                        item.itemId,
                                                        item.quantity
                                                    )
                                                )
                                            },
                                            onIncrement = {
                                                onEvent(
                                                    CartUiEvent.IncreaseQuantity(
                                                        item.itemId,
                                                        item.quantity
                                                    )
                                                )
                                            }
                                        )
                                    }
                                }
                            )
                        }
                        if (uiState.items.isNotEmpty()) {
                            item {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(Theme.space.large),
                                    verticalArrangement = Arrangement.spacedBy(Theme.space.medium)
                                ) {
                                    CartSummarySection(uiState = uiState, onEvent = onEvent)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
