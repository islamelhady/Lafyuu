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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.elhady.lafyuu.core.designsystem.components.appbar.ProductTopBar
import com.elhady.lafyuu.core.designsystem.components.button.DefaultButton
import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.core.designsystem.components.element.InfoStateContent
import com.elhady.lafyuu.core.designsystem.components.element.LafyuuScaffold
import com.elhady.lafyuu.core.designsystem.components.other.LafyuuSnackBarVisuals
import com.elhady.lafyuu.core.designsystem.icons.More
import com.elhady.lafyuu.core.designsystem.icons.Search
import com.elhady.lafyuu.core.designsystem.theme.Theme
import com.elhady.lafyuu.core.ui.text.asString
import com.elhady.lafyuu.feature.product.R
import com.elhady.lafyuu.feature.product.presentation.components.ProductDetailsContent
import kotlinx.coroutines.launch

@Composable
fun ProductDetailsRoute(
    onNavigateBack: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToProductDetails: (String) -> Unit,
    onNavigateToReviews: (String) -> Unit,
    onNavigateToWriteReview: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProductDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(viewModel, lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.uiEffect.collect { effect ->
                when (effect) {
                    ProductDetailsUiEffect.NavigateBack -> onNavigateBack()
                    ProductDetailsUiEffect.NavigateToSearch -> onNavigateToSearch()
                    is ProductDetailsUiEffect.NavigateToProductDetails -> onNavigateToProductDetails(
                        effect.productId
                    )

                    is ProductDetailsUiEffect.NavigateToReviews -> onNavigateToReviews(effect.productId)
                    is ProductDetailsUiEffect.NavigateToWriteReview -> onNavigateToWriteReview(
                        effect.productId
                    )

                    is ProductDetailsUiEffect.ShowSnackbar -> {
                        snackbarHostState.currentSnackbarData?.dismiss()
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

    ProductDetailsScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onIntent = viewModel::onIntent,
        modifier = modifier
    )
}

@Composable
fun ProductDetailsScreen(
    uiState: ProductDetailsUiState,
    snackbarHostState: SnackbarHostState,
    onIntent: (ProductDetailsUiIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    LafyuuScaffold(
        snackbarHostState = snackbarHostState,
        topBar = {
            ProductTopBar(
                title = uiState.product?.name ?: "Product Details",
                leadingIcon = Left,
                onLeadingClick = { onIntent(ProductDetailsUiIntent.BackClicked) },
                searchIcon = Search,
                onSearchClick = { onIntent(ProductDetailsUiIntent.SearchClicked) },
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
                        caption = stringResource(
                            if (uiState.isInCart) R.string.product_remove_from_cart
                            else R.string.product_add_to_cart
                        ),
                        isLoading = uiState.isAddingToCart,
                        onClick = { onIntent(ProductDetailsUiIntent.AddToCartClicked) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Theme.color.blue
                    )
                }

                uiState.error != null -> {
                    InfoStateContent(
                        errorMessage = uiState.error.asString(),
                        onRetryClick = { onIntent(ProductDetailsUiIntent.RetryClicked) },
                        errorType = AlertType.Error,
                    )
                }

                uiState.product != null -> {
                    ProductDetailsContent(
                        uiState = uiState,
                        onIntent = onIntent,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}
