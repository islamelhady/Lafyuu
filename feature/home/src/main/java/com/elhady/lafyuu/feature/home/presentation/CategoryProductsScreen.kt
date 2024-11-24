package com.elhady.lafyuu.feature.home.presentation

import Left
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.elhady.lafyuu.core.designsystem.components.appbar.SingleTopAppBar
import com.elhady.lafyuu.core.designsystem.components.element.InfoStateContent
import com.elhady.lafyuu.core.designsystem.components.element.LafyuuScaffold
import com.elhady.lafyuu.core.designsystem.theme.Theme
import com.elhady.lafyuu.core.ui.text.asString
import com.elhady.lafyuu.feature.home.R
import com.elhady.lafyuu.feature.home.presentation.components.CollectEffects
import com.elhady.lafyuu.feature.home.presentation.components.ProductsGrid

@Composable
fun CategoryProductsRoute(
    onNavigateBack: () -> Unit,
    onNavigateToProductDetails: (String) -> Unit,
    viewModel: CategoryProductsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CollectEffects(viewModel.uiEffect) { effect ->
        when (effect) {
            CategoryProductsUiEffect.NavigateBack -> onNavigateBack()
            is CategoryProductsUiEffect.NavigateToProductDetails ->
                onNavigateToProductDetails(effect.productId)
        }
    }

    CategoryProductsScreen(uiState = uiState, onIntent = viewModel::onIntent)
}

@Composable
fun CategoryProductsScreen(
    uiState: CategoryProductsUiState,
    onIntent: (CategoryProductsUiIntent) -> Unit
) {
    LafyuuScaffold(
        topBar = {
            SingleTopAppBar(
                title = uiState.categoryName,
                leadingIcon = Left,
                onLeadingClick = { onIntent(CategoryProductsUiIntent.BackClicked) }
            )
        }
    ) {
        Box(modifier = Modifier.fillMaxSize().padding(Theme.space.large)) {
            when {
                uiState.isLoading -> CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Theme.color.blue
                )

                uiState.error != null -> InfoStateContent(
                    errorMessage = uiState.error.asString(),
                    onRetryClick = { onIntent(CategoryProductsUiIntent.RetryClicked) },
                    modifier = Modifier.align(Alignment.Center)
                )

                uiState.products.isEmpty() -> InfoStateContent(
                    errorMessage = stringResource(R.string.home_no_products),
                    onRetryClick = { onIntent(CategoryProductsUiIntent.RetryClicked) },
                    modifier = Modifier.align(Alignment.Center)
                )

                else -> ProductsGrid(
                    products = uiState.products,
                    onProductClick = { onIntent(CategoryProductsUiIntent.ProductClicked(it)) }
                )
            }
        }
    }
}