package com.elhady.lafyuu.feature.search.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.elhady.lafyuu.core.designsystem.components.appbar.SearchBarWithTrailing
import com.elhady.lafyuu.core.designsystem.components.appbar.SingleTopAppBar
import com.elhady.lafyuu.core.designsystem.components.button.DefaultButton
import com.elhady.lafyuu.core.designsystem.components.button.LabelButton
import com.elhady.lafyuu.core.designsystem.components.card.ProductCard
import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.core.designsystem.components.element.InfoStateContent
import com.elhady.lafyuu.core.designsystem.components.element.LafyuuScaffold
import com.elhady.lafyuu.core.designsystem.components.element.VerticalSpacerMedium
import com.elhady.lafyuu.core.designsystem.components.other.LafyuuSlider
import com.elhady.lafyuu.core.designsystem.components.other.LafyuuSnackBarVisuals
import com.elhady.lafyuu.core.designsystem.components.text.LafyuuText
import com.elhady.lafyuu.core.designsystem.components.textfield.DefaultTextField
import com.elhady.lafyuu.core.designsystem.icons.Filter
import com.elhady.lafyuu.core.designsystem.icons.Short
import com.elhady.lafyuu.core.designsystem.icons.X
import com.elhady.lafyuu.core.designsystem.theme.Theme

@Composable
fun SearchRoute(
    onNavigateToHome: () -> Unit,
    onNavigateToProductDetails: (String) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is SearchUiEffect.NavigateToProductDetails -> onNavigateToProductDetails(effect.productId)
                SearchUiEffect.NavigateToHome -> onNavigateToHome()
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    uiState: SearchUiState,
    snackbarHostState: SnackbarHostState,
    onEvent: (SearchUiEvent) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()

    LafyuuScaffold(
        snackbarHostState = snackbarHostState,
        topBar = {
            SearchBarWithTrailing(
                searchValue = uiState.query,
                onSearchValueChange = { onEvent(SearchUiEvent.QueryChanged(it)) },
                onClearSearchClick = { onEvent(SearchUiEvent.QueryChanged("")) },
                trailingIcon = Short,
                onTrailingClick = { onEvent(SearchUiEvent.SortClicked) },
                filterIcon = Filter,
                onFilterClick = { onEvent(SearchUiEvent.FilterClicked) },
            )
        }
    ) {
        when {
            uiState.query.isBlank() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    InfoStateContent(
                        errorMessage = "Type a product name to search",
                        errorType = AlertType.Warning,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            uiState.isLoading && uiState.products.isEmpty() -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Theme.color.blue
                )
            }

            uiState.products.isEmpty() -> {
                InfoStateContent(
                    errorMessage = "Product Not Found",
                    caption = "Back to Home",
                    onRetryClick = { onEvent(SearchUiEvent.BackToHomeClick) },
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

        if (uiState.isFilterSheetOpen) {
            ModalBottomSheet(
                onDismissRequest = { onEvent(SearchUiEvent.FilterDismissed) },
                sheetState = sheetState
            ) {
                SingleTopAppBar(
                    title = "Filter Search",
                    leadingIcon = X,
                    onLeadingClick = { onEvent(SearchUiEvent.FilterDismissed) }
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Theme.space.large),
                    verticalArrangement = Arrangement.spacedBy(Theme.space.medium)
                ) {
                    LafyuuText(
                        text = "Price Range",
                        style = Theme.typography.heading5,
                        color = Theme.color.neutralDark
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(Theme.space.medium),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        DefaultTextField(
                            value = "${uiState.tempMinPrice?.toInt() ?: 0}",
                            onValueChange = { newValue ->
                                onEvent(
                                    SearchUiEvent.PriceRangeChanged(
                                        newValue.toDoubleOrNull() ?: 0.0,
                                        uiState.tempMaxPrice ?: 2000.0
                                    )
                                )
                            },
                            modifier = Modifier.weight(1f)
                        )
                        DefaultTextField(
                            value = "${uiState.tempMaxPrice?.toInt() ?: 2000}",
                            onValueChange = { newValue ->
                                onEvent(
                                    SearchUiEvent.PriceRangeChanged(
                                        uiState.tempMinPrice ?: 0.0,
                                        newValue.toDoubleOrNull() ?: 2000.0
                                    )
                                )
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    LafyuuSlider(
                        value = (uiState.tempMinPrice?.toFloat()
                            ?: 0f)..(uiState.tempMaxPrice?.toFloat() ?: 2000f),
                        onValueChange = { range ->
                            onEvent(
                                SearchUiEvent.PriceRangeChanged(
                                    range.start.toDouble(),
                                    range.endInclusive.toDouble()
                                )
                            )
                        },
                        valueRange = 0f..2000f,
                        modifier = Modifier.fillMaxWidth()
                    )

                    LafyuuText(
                        text = "Availability",
                        style = Theme.typography.heading5,
                        color = Theme.color.neutralDark
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(Theme.space.medium)
                    ) {
                        LabelButton(
                            caption = "All",
                            isEnabled = uiState.tempIsInStock == null,
                            onClick = { onEvent(SearchUiEvent.InStockChanged(null)) },
                            hasBorder = true
                        )
                        LabelButton(
                            caption = "In Stock",
                            isEnabled = uiState.tempIsInStock == true,
                            onClick = { onEvent(SearchUiEvent.InStockChanged(true)) },
                            hasBorder = true
                        )
                    }
                    DefaultButton(
                        caption = "Apply",
                        onClick = { onEvent(SearchUiEvent.ApplyFilters) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = Theme.space.large)
                    )
                    VerticalSpacerMedium()
                }
            }
        }
    }
}
