package com.elhady.lafyuu.feature.home.presentation

import Left
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.elhady.lafyuu.core.designsystem.components.appbar.SingleTopAppBar
import com.elhady.lafyuu.core.designsystem.components.card.ProductCard
import com.elhady.lafyuu.core.designsystem.components.element.InfoStateContent
import com.elhady.lafyuu.core.designsystem.components.element.LafyuuScaffold
import com.elhady.lafyuu.core.designsystem.components.other.LafyuuSnackBarVisuals
import com.elhady.lafyuu.core.designsystem.theme.Theme
import com.elhady.lafyuu.core.ui.text.asString
import kotlinx.coroutines.flow.collectLatest
import java.util.Locale

@Composable
fun CategoryProductsRoute(
    onNavigateBack: () -> Unit,
    onNavigateToProductDetails: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CategoryProductsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                is CategoryProductsUiEffect.NavigateToProductDetails -> onNavigateToProductDetails(effect.productId)
                CategoryProductsUiEffect.NavigateBack -> onNavigateBack()
                is CategoryProductsUiEffect.ShowSnackbar -> {
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

    CategoryProductsScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        snackbarHostState = snackbarHostState,
        modifier = modifier
    )
}

@Composable
fun CategoryProductsScreen(
    uiState: CategoryProductsUiState,
    onEvent: (CategoryProductsUiIntent) -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    LafyuuScaffold(
        snackbarHostState = snackbarHostState,
        topBar = {
            SingleTopAppBar(
                title = uiState.categoryName.ifBlank { "Category Products" },
                leadingIcon = Left,
                onLeadingClick = { onEvent(CategoryProductsUiIntent.BackClicked) }
            )
        }
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(Theme.space.large)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Theme.color.blue
                    )
                }

                uiState.error != null && uiState.products.isEmpty() -> {
                    InfoStateContent(
                        errorMessage = uiState.error.asString(),
                        onRetryClick = { onEvent(CategoryProductsUiIntent.RetryClicked) },
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                uiState.products.isEmpty() -> {
                    InfoStateContent(
                        errorMessage = "No products found in this category",
                        onRetryClick = { onEvent(CategoryProductsUiIntent.RetryClicked) },
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = Theme.space.large),
                        horizontalArrangement = Arrangement.spacedBy(Theme.space.medium),
                        verticalArrangement = Arrangement.spacedBy(Theme.space.medium)
                    ) {
                        items(uiState.products, key = { it.id }) { product ->
                            ProductCard(
                                imageUrl = product.imageUrl,
                                name = product.name,
                                price = "$${String.format(Locale.US, "%.2f", product.price)}",
                                originalPrice = product.originalPrice?.let {
                                    "$${String.format(Locale.US, "%.2f", it)}"
                                },
                                discountLabel = product.discountPercentage?.let { if ((it  > 0.0)) "${product.discountPercentage}% OFF" else null },
                                rating = product.rating,
                                onClick = { onEvent(CategoryProductsUiIntent.ProductClicked(product.id)) }
                            )
                        }
                    }
                }
            }
        }
    }
}
