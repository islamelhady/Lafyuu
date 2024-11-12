package com.elhady.lafyuu.feature.favorites.presentation

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
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.elhady.lafyuu.core.designsystem.components.appbar.SingleTopAppBar
import com.elhady.lafyuu.core.designsystem.components.card.ProductCard
import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.core.designsystem.components.element.InfoStateContent
import com.elhady.lafyuu.core.designsystem.components.element.LafyuuScaffold
import com.elhady.lafyuu.core.designsystem.components.other.LafyuuSnackBarVisuals
import com.elhady.lafyuu.core.designsystem.theme.Theme
import com.elhady.lafyuu.feature.favorites.R

@Composable
fun FavoritesRoute(
    onNavigateBack: () -> Unit,
    onNavigateToProductDetails: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(viewModel, lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.uiEffect.collect { effect ->
                when (effect) {
                    FavoritesUiEffect.NavigateBack -> onNavigateBack()
                    is FavoritesUiEffect.NavigateToProductDetails -> onNavigateToProductDetails(
                        effect.productId
                    )

                    is FavoritesUiEffect.ShowSnackbar -> {
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

    FavoritesScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onIntent = viewModel::onIntent,
        modifier = modifier
    )
}

@Composable
fun FavoritesScreen(
    uiState: FavoritesUiState,
    snackbarHostState: SnackbarHostState,
    onIntent: (FavoritesUiIntent) -> Unit,
    modifier: Modifier = Modifier.padding(Theme.space.large)
) {
    Box(modifier = modifier) {
        LafyuuScaffold(
            snackbarHostState = snackbarHostState,
            topBar = {
                SingleTopAppBar(
                    title = stringResource(R.string.favorites_title),
                    leadingIcon = Left,
                    onLeadingClick = { onIntent(FavoritesUiIntent.BackClicked) }
                )
            }
        ) {
            when {
                uiState.isLoading -> CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Theme.color.blue
                )

                uiState.isEmpty -> InfoStateContent(
                    errorMessage = stringResource(R.string.favorites_empty),
                    errorType = AlertType.Warning,
                    modifier = Modifier.align(Alignment.Center)
                )

                else -> LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = Theme.space.large),
                    verticalArrangement = Arrangement.spacedBy(Theme.space.medium)
                ) {
                    items(items = uiState.products, key = { it.id }) { product ->
                        ProductCard(
                            imageUrl = product.imageUrl,
                            name = product.name,
                            price = product.price,
                            originalPrice = product.originalPrice,
                            discountLabel = if (product.discountPercentage > 0) {
                                stringResource(
                                    R.string.favorites_discount_label,
                                    product.discountPercentage
                                )
                            } else null,
                            rating = product.rating,
                            isDelete = true,
                            onDeleteClick = {
                                onIntent(FavoritesUiIntent.RemoveFavoriteClicked(product.id))
                            },
                            onClick = { onIntent(FavoritesUiIntent.ProductClicked(product.id)) }
                        )
                    }
                }
            }
        }
    }
}