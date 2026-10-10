package com.elhady.lafyuu.feature.home.presentation

import Left
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.elhady.lafyuu.core.designsystem.components.appbar.SingleTopAppBar
import com.elhady.lafyuu.core.designsystem.components.element.InfoStateContent
import com.elhady.lafyuu.core.designsystem.components.element.LafyuuScaffold
import com.elhady.lafyuu.core.designsystem.components.list.SingleListItem
import com.elhady.lafyuu.core.designsystem.components.other.LafyuuSnackBarVisuals
import com.elhady.lafyuu.core.designsystem.components.other.ProductCategory
import com.elhady.lafyuu.core.designsystem.icons.X
import com.elhady.lafyuu.core.designsystem.theme.Theme
import com.elhady.lafyuu.feature.home.presentation.components.getCategoryIcon

@Composable
fun CategoriesRoute(
    onNavigateBack: () -> Unit,
    onNavigateToCategoryProducts: (String, String) -> Unit,
    viewModel: CategoriesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                CategoriesUiEffect.NavigateBack -> onNavigateBack()
                is CategoriesUiEffect.NavigateToCategoryProducts -> onNavigateToCategoryProducts(
                    effect.categoryId,
                    effect.categoryName
                )

                is CategoriesUiEffect.ShowSnackbar -> {
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

    CategoriesScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onEvent = viewModel::onEvent,
    )
}

@Composable
fun CategoriesScreen(
    uiState: CategoriesUiState,
    snackbarHostState: SnackbarHostState,
    onEvent: (CategoriesUiEvent) -> Unit,
) {
    LafyuuScaffold(
        snackbarHostState = snackbarHostState,
        topBar = {
            SingleTopAppBar(
                title = "Categories",
                leadingIcon = Left,
                onLeadingClick = { onEvent(CategoriesUiEvent.BackClicked) }
            )
        }
    ) {
        when {
            uiState.isLoading && uiState.categories.isEmpty() -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Theme.color.blue
                )
            }

            uiState.categories.isEmpty() && !uiState.isLoading -> {
                InfoStateContent(
                    errorMessage = uiState.error ?: "No categories found",
                    onRetryClick = { onEvent(CategoriesUiEvent.RetryClicked) },
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            else -> {
                LazyColumn(
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
                                onEvent(
                                    CategoriesUiEvent.SelectCategory(
                                        categoryId = category.id,
                                        categoryName = category.name
                                    )
                                )
                            },
                        )
                    }
                }
            }
        }
    }
}