package com.elhady.lafyuu.feature.search.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.elhady.lafyuu.core.designsystem.components.appbar.SearchBarWithTrailing
import com.elhady.lafyuu.core.designsystem.components.card.ProductCard
import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.core.designsystem.components.element.InfoStateContent
import com.elhady.lafyuu.core.designsystem.components.element.LafyuuScaffold
import com.elhady.lafyuu.core.designsystem.components.other.LafyuuSnackBarVisuals
import com.elhady.lafyuu.core.designsystem.icons.Filter
import com.elhady.lafyuu.core.designsystem.icons.Short
import com.elhady.lafyuu.core.designsystem.theme.Theme

@Composable
fun SearchRoute(
    onNavigateBack: () -> Unit,
    onNavigateToProductDetails: (String) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is SearchUiEffect.NavigateToProductDetails -> onNavigateToProductDetails(effect.productId)
                SearchUiEffect.NavigateBack -> onNavigateBack()
                is SearchUiEffect.ShowSnackbar -> {
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

    SearchScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onEvent = viewModel::onEvent,
    )
}

@SuppressLint("DefaultLocale")
@Composable
fun SearchScreen(
    uiState: SearchUiState,
    snackbarHostState: SnackbarHostState,
    onEvent: (SearchUiEvent) -> Unit,
) {
    LafyuuScaffold(
        snackbarHostState = snackbarHostState,
        topBar = {
            SearchBarWithTrailing(
                searchValue = uiState.query,
                onSearchValueChange = { onEvent(SearchUiEvent.QueryChanged(it)) },
                onClearSearchClick = { onEvent(SearchUiEvent.QueryChanged("")) },
                trailingIcon = Short,
                onTrailingClick = {},
                filterIcon = Filter,
                onFilterClick = {},
            )
        }
    ) {
        when {
            uiState.isLoading && uiState.products.isEmpty() -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Theme.color.blue
                )
            }

            uiState.products.isEmpty() -> {
                InfoStateContent(
                    errorMessage = "Product Not Found",
                    onRetryClick = { onEvent(SearchUiEvent.RetryClicked) },
                    errorType = AlertType.Error,
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
                            imageUrl = product.coverPictureUrl,
                            name = product.name,
                            price = "$${String.format("%.2f", product.price)}",
                            originalPrice = product.originalPrice?.let {
                                "$${String.format("%.2f", it)}"
                            },
                            discountLabel = product.discountLabel,
                            rating = product.rating,
                            onClick = { onEvent(SearchUiEvent.ProductClicked(product.id)) }
                        )
                    }
                }
            }
        }
    }
}
