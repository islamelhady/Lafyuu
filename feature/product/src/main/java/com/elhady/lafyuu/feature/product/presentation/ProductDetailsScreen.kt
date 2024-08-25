package com.elhady.lafyuu.feature.product.presentation

import Left
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.elhady.lafyuu.core.designsystem.components.appbar.ProductTopBar
import com.elhady.lafyuu.core.designsystem.components.button.DefaultButton
import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.core.designsystem.components.element.InfoStateContent
import com.elhady.lafyuu.core.designsystem.components.element.LafyuuScaffold
import com.elhady.lafyuu.core.designsystem.components.other.LafyuuSnackBarVisuals
import com.elhady.lafyuu.core.designsystem.icons.More
import com.elhady.lafyuu.core.designsystem.icons.Search
import com.elhady.lafyuu.core.designsystem.theme.Theme
import com.elhady.lafyuu.feature.product.presentation.components.ProductDetailsContent

@Composable
fun ProductDetailsRoute(
    productId: String,
    onNavigateBack: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToProductDetails: (String) -> Unit,
    onNavigateToReviews: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProductDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                ProductDetailsUiEffect.NavigateBack -> onNavigateBack()
                ProductDetailsUiEffect.NavigateToSearch -> onNavigateToSearch()
                is ProductDetailsUiEffect.NavigateToProductDetails -> onNavigateToProductDetails(
                    effect.productId
                )
                is ProductDetailsUiEffect.NavigateToReviews -> onNavigateToReviews(effect.productId)

                is ProductDetailsUiEffect.ShowSnackbar -> {
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

    ProductDetailsScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onEvent = viewModel::onEvent,
        modifier = modifier
    )
}

@Composable
fun ProductDetailsScreen(
    uiState: ProductDetailsUiState,
    snackbarHostState: SnackbarHostState,
    onEvent: (ProductDetailsUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    LafyuuScaffold(
        snackbarHostState = snackbarHostState,
        topBar = {
            ProductTopBar(
                title = uiState.product?.name ?: "Product Details",
                leadingIcon = Left,
                onLeadingClick = { onEvent(ProductDetailsUiEvent.BackClicked) },
                searchIcon = Search,
                onSearchClick = { onEvent(ProductDetailsUiEvent.SearchClicked) },
                trailingIcon = More,
                onTrailingClick = { },
            )
        },
        bottomBar = {
            if (uiState.product != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Theme.color.backgroundWhite)
                        .padding(Theme.space.large)
                ) {
                    DefaultButton(
                        caption = "Add To Cart",
                        isLoading = uiState.isAddingToCart,
                        onClick = { onEvent(ProductDetailsUiEvent.AddToCartClicked) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            when {
                uiState.isLoading && uiState.product == null -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Theme.color.blue
                    )
                }

                uiState.error != null && uiState.product == null -> {
                    InfoStateContent(
                        errorMessage = uiState.error,
                        onRetryClick = { onEvent(ProductDetailsUiEvent.RetryClicked) },
                        errorType = AlertType.Error,
                    )
                }
                uiState.product != null -> {
                    ProductDetailsContent(
                        uiState = uiState,
                        onEvent = onEvent,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}
