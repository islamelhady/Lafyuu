package com.elhady.lafyuu.feature.favorites.presentation

import Left
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.elhady.lafyuu.core.designsystem.components.appbar.SingleTopAppBar
import com.elhady.lafyuu.core.designsystem.components.card.ProductCard
import com.elhady.lafyuu.core.designsystem.components.card.WideProductCard
import com.elhady.lafyuu.core.designsystem.components.element.InfoStateContent
import com.elhady.lafyuu.core.designsystem.components.element.LafyuuScaffold
import com.elhady.lafyuu.core.designsystem.components.other.LafyuuSnackBarVisuals
import com.elhady.lafyuu.core.designsystem.theme.Theme

@Composable
fun FavoritesRoute(
    onNavigateBack: () -> Unit,
    onNavigateToProductDetails: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                FavoritesUiEffect.NavigateBack -> onNavigateBack()
                is FavoritesUiEffect.NavigateToProductDetails -> onNavigateToProductDetails(effect.productId)
                is FavoritesUiEffect.ShowSnackbar -> {
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

    FavoritesScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onEvent = viewModel::onEvent,
        modifier = modifier
    )
}

@Composable
fun FavoritesScreen(
    uiState: FavoritesUiState,
    snackbarHostState: SnackbarHostState,
    onEvent: (FavoritesUiEvent) -> Unit,
    modifier: Modifier = Modifier.padding(Theme.space.large)
) {
    LafyuuScaffold(
        snackbarHostState = snackbarHostState,
        topBar = {
            SingleTopAppBar(
                title = "Favorite Product",
                leadingIcon = Left,
                onLeadingClick = { onEvent(FavoritesUiEvent.BackClicked) }
            )
        }
    ) {
        when {
            uiState.isLoading && uiState.favoriteProducts.isEmpty() -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Theme.color.blue
                )
            }

            uiState.error != null && uiState.favoriteProducts.isEmpty() -> {
                InfoStateContent(
                    errorMessage = uiState.error,
                    onRetryClick = { onEvent(FavoritesUiEvent.LoadFavorites) },
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            uiState.favoriteProducts.isEmpty() -> {
                InfoStateContent(
                    errorMessage = "No favorite products yet",
                    onRetryClick = { onEvent(FavoritesUiEvent.LoadFavorites) },
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = Theme.space.large),
                    verticalArrangement = Arrangement.spacedBy(Theme.space.medium)
                ) {
                    items(
                        items = uiState.favoriteProducts,
                        key = { it.id }
                    ) { product ->
                        ProductCard(
                            imageUrl = product.coverPictureUrl.ifBlank { product.productPictures.firstOrNull() },
                            name = product.name,
                            price = "$${"%.2f".format(product.price)}",
                            rating = product.rating,
                            discountLabel = product.discountPercentage.toString() + "% OFF",
                            originalPrice = "$${"%.2f".format(product.originalPrice)}",
                            isDelete = true,
                            onDeleteClick = { onEvent(FavoritesUiEvent.RemoveFavorite(product.id)) },
                            onClick = { onEvent(FavoritesUiEvent.ProductClicked(product.id)) }
                        )
                    }
                }
            }
        }
    }
}
