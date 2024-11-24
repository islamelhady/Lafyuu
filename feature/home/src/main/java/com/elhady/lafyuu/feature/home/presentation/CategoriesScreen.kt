package com.elhady.lafyuu.feature.home.presentation

import Left
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.elhady.lafyuu.core.designsystem.components.list.SingleListItem
import com.elhady.lafyuu.core.designsystem.theme.Theme
import com.elhady.lafyuu.core.ui.text.asString
import com.elhady.lafyuu.feature.home.R
import com.elhady.lafyuu.feature.home.presentation.components.CollectEffects
import com.elhady.lafyuu.feature.home.presentation.components.getCategoryIcon

@Composable
fun CategoriesRoute(
    onNavigateBack: () -> Unit,
    onNavigateToCategoryProducts: (String, String) -> Unit,
    viewModel: CategoriesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CollectEffects(viewModel.uiEffect) { effect ->
        when (effect) {
            CategoriesUiEffect.NavigateBack -> onNavigateBack()
            is CategoriesUiEffect.NavigateToCategoryProducts ->
                onNavigateToCategoryProducts(effect.categoryId, effect.categoryName)
        }
    }

    CategoriesScreen(uiState = uiState, onIntent = viewModel::onIntent)
}

@Composable
fun CategoriesScreen(
    uiState: CategoriesUiState,
    onIntent: (CategoriesUiIntent) -> Unit
) {
    LafyuuScaffold(
        topBar = {
            SingleTopAppBar(
                title = stringResource(R.string.home_categories_title),
                leadingIcon = Left,
                onLeadingClick = { onIntent(CategoriesUiIntent.BackClicked) }
            )
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            when {
                uiState.isLoading -> CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Theme.color.blue
                )

                uiState.error != null -> InfoStateContent(
                    errorMessage = uiState.error.asString(),
                    onRetryClick = { onIntent(CategoriesUiIntent.RetryClicked) },
                    modifier = Modifier.align(Alignment.Center)
                )

                uiState.categories.isEmpty() -> InfoStateContent(
                    errorMessage = stringResource(R.string.home_no_categories),
                    onRetryClick = { onIntent(CategoriesUiIntent.RetryClicked) },
                    modifier = Modifier.align(Alignment.Center)
                )

                else -> LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = Theme.space.large),
                    verticalArrangement = Arrangement.spacedBy(Theme.space.medium)
                ) {
                    items(uiState.categories, key = { it.id }) { category ->
                        SingleListItem(
                            title = category.name.replaceFirstChar { it.uppercase() },
                            leadingIcon = getCategoryIcon(categoryName = category.name),
                            leadingIconTint = Theme.color.blue,
                            onClick = {
                                onIntent(CategoriesUiIntent.CategoryClicked(category.id, category.name))
                            }
                        )
                    }
                }
            }
        }
    }
}