package com.elhady.lafyuu.feature.search.presentation

import Left
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
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
import com.elhady.lafyuu.core.designsystem.icons.Sort
import com.elhady.lafyuu.core.designsystem.icons.X
import com.elhady.lafyuu.core.designsystem.theme.Theme
import com.elhady.lafyuu.core.ui.text.asString
import com.elhady.lafyuu.feature.search.R
import com.elhady.lafyuu.feature.search.domain.model.ProductSort
import com.elhady.lafyuu.feature.search.presentation.model.ProductUi
import kotlinx.coroutines.launch

private const val LOAD_MORE_THRESHOLD = 4

@Composable
fun SearchRoute(
    onNavigateToHome: () -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToProductDetails: (String) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(viewModel, lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.uiEffect.collect { effect ->
                when (effect) {
                    is SearchUiEffect.NavigateToProductDetails ->
                        onNavigateToProductDetails(effect.productId)
                    SearchUiEffect.NavigateToHome -> onNavigateToHome()
                    SearchUiEffect.NavigateBack -> onNavigateBack()

                    is SearchUiEffect.ShowSnackbar -> {
                        snackbarHostState.currentSnackbarData?.dismiss()
                        // separate coroutine: showSnackbar suspends until dismissed
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

    SearchScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onIntent = viewModel::onIntent
    )
}

@Composable
fun SearchScreen(
    uiState: SearchUiState,
    snackbarHostState: SnackbarHostState,
    onIntent: (SearchUiIntent) -> Unit
) {
    LafyuuScaffold(
        snackbarHostState = snackbarHostState,
        topBar = {
            SearchBarWithTrailing(
                searchValue = uiState.query,
                onSearchValueChange = { onIntent(SearchUiIntent.QueryChanged(it)) },
                onClearSearchClick = { onIntent(SearchUiIntent.QueryChanged("")) },
                leadingIcon = Left,
                onLeadingClick = { onIntent(SearchUiIntent.BackClicked) },
                trailingIcon = Sort,
                onTrailingClick = { onIntent(SearchUiIntent.SortClicked) },
                filterIcon = Filter,
                onFilterClick = { onIntent(SearchUiIntent.FilterClicked) }
            )
        }
    ) {
        when {
            uiState.query.isBlank() -> InfoStateContent(
                errorMessage = stringResource(R.string.search_prompt),
                errorType = AlertType.Warning,
                modifier = Modifier.align(Alignment.Center)
            )

            uiState.isSearchPending -> CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Theme.color.blue
            )

            uiState.error != null -> InfoStateContent(
                errorMessage = uiState.error.asString(),
                onRetryClick = { onIntent(SearchUiIntent.RetryClicked) },
                errorType = AlertType.Error,
                modifier = Modifier.align(Alignment.Center)
            )

            uiState.products.isEmpty() -> InfoStateContent(
                errorMessage = stringResource(R.string.search_not_found),
                caption = stringResource(R.string.search_back_to_home),
                onRetryClick = { onIntent(SearchUiIntent.BackToHomeClicked) },
                errorType = AlertType.Error,
                modifier = Modifier.align(Alignment.Center)
            )

            else -> ResultsGrid(
                products = uiState.products,
                isLoadingMore = uiState.isLoadingMore,
                onIntent = onIntent
            )
        }

        when (uiState.sheet) {
            SearchSheet.Filter -> FilterSheet(uiState, onIntent)
            SearchSheet.Sort -> SortSheet(uiState.sort, onIntent)
            SearchSheet.None -> Unit
        }
    }
}

@Composable
private fun ResultsGrid(
    products: List<ProductUi>,
    isLoadingMore: Boolean,
    onIntent: (SearchUiIntent) -> Unit
) {
    val gridState = rememberLazyGridState()
    val isNearEnd by remember(gridState) {
        derivedStateOf {
            val info = gridState.layoutInfo
            val lastVisible = info.visibleItemsInfo.lastOrNull()?.index ?: 0
            info.totalItemsCount > 0 && lastVisible >= info.totalItemsCount - LOAD_MORE_THRESHOLD
        }
    }
    // keyed on size: after a page is appended the condition is evaluated again
    LaunchedEffect(isNearEnd, products.size) {
        if (isNearEnd) onIntent(SearchUiIntent.LoadMore)
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        state = gridState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = Theme.space.large),
        horizontalArrangement = Arrangement.spacedBy(Theme.space.medium),
        verticalArrangement = Arrangement.spacedBy(Theme.space.medium)
    ) {
        items(products, key = { it.id }) { product ->
            ProductCard(
                imageUrl = product.imageUrl,
                name = product.name,
                price = product.price,
                originalPrice = product.originalPrice,
                discountLabel = if (product.discountPercentage > 0) {
                    stringResource(R.string.search_discount_label, product.discountPercentage)
                } else null,
                rating = product.rating,
                onClick = { onIntent(SearchUiIntent.ProductClicked(product.id)) }
            )
        }
        if (isLoadingMore) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Theme.space.medium),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Theme.color.blue)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun FilterSheet(
    uiState: SearchUiState,
    onIntent: (SearchUiIntent) -> Unit
) {
    val draft = uiState.draft
    ModalBottomSheet(
        onDismissRequest = { onIntent(SearchUiIntent.SheetDismissed) },
        sheetState = rememberModalBottomSheetState()
    ) {
        SingleTopAppBar(
            title = stringResource(R.string.search_filter_title),
            leadingIcon = X,
            onLeadingClick = { onIntent(SearchUiIntent.SheetDismissed) }
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Theme.space.large)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(Theme.space.medium)
        ) {
            SheetSectionTitle(stringResource(R.string.search_price_range))
            Row(
                horizontalArrangement = Arrangement.spacedBy(Theme.space.medium),
                verticalAlignment = Alignment.CenterVertically
            ) {
                DefaultTextField(
                    value = draft.minPrice.toString(),
                    onValueChange = {
                        onIntent(
                            SearchUiIntent.PriceRangeChanged(
                                min = it.toIntOrNull() ?: PRICE_RANGE_MIN,
                                max = draft.maxPrice
                            )
                        )
                    },
                    modifier = Modifier.weight(1f)
                )
                DefaultTextField(
                    value = draft.maxPrice.toString(),
                    onValueChange = {
                        onIntent(
                            SearchUiIntent.PriceRangeChanged(
                                min = draft.minPrice,
                                max = it.toIntOrNull() ?: PRICE_RANGE_MAX
                            )
                        )
                    },
                    modifier = Modifier.weight(1f)
                )
            }
            // the user can type min > max, the slider always gets an ordered range
            val low = minOf(draft.minPrice, draft.maxPrice)
            val high = maxOf(draft.minPrice, draft.maxPrice)
            LafyuuSlider(
                value = low.toFloat()..high.toFloat(),
                onValueChange = { range ->
                    onIntent(
                        SearchUiIntent.PriceRangeChanged(
                            min = range.start.toInt(),
                            max = range.endInclusive.toInt()
                        )
                    )
                },
                valueRange = PRICE_RANGE_MIN.toFloat()..PRICE_RANGE_MAX.toFloat(),
                modifier = Modifier.fillMaxWidth()
            )

            SheetSectionTitle(stringResource(R.string.search_category))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(Theme.space.small),
                verticalArrangement = Arrangement.spacedBy(Theme.space.small)
            ) {
                SelectableChip(
                    caption = stringResource(R.string.search_all),
                    selected = draft.category == null,
                    onClick = { onIntent(SearchUiIntent.CategorySelected(null)) }
                )
                uiState.categories.forEach { name ->
                    SelectableChip(
                        caption = name,
                        selected = draft.category == name,
                        onClick = { onIntent(SearchUiIntent.CategorySelected(name)) }
                    )
                }
            }

            SheetSectionTitle(stringResource(R.string.search_availability))
            Row(horizontalArrangement = Arrangement.spacedBy(Theme.space.medium)) {
                SelectableChip(
                    caption = stringResource(R.string.search_all),
                    selected = !draft.inStockOnly,
                    onClick = { onIntent(SearchUiIntent.InStockChanged(false)) }
                )
                SelectableChip(
                    caption = stringResource(R.string.search_in_stock_only),
                    selected = draft.inStockOnly,
                    onClick = { onIntent(SearchUiIntent.InStockChanged(true)) }
                )
            }

            DefaultButton(
                caption = stringResource(R.string.search_apply),
                onClick = { onIntent(SearchUiIntent.ApplyFilters) },
                modifier = Modifier.fillMaxWidth()
            )
            LabelButton(
                caption = stringResource(R.string.search_reset),
                onClick = { onIntent(SearchUiIntent.ResetFilters) },
                contentColor = Theme.color.blue,
                hasBorder = true,
                borderColor = Theme.color.blue,
                style = Theme.typography.largeCaptionBold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = Theme.space.large)
            )
            VerticalSpacerMedium()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SortSheet(
    selected: ProductSort?,
    onIntent: (SearchUiIntent) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = { onIntent(SearchUiIntent.SheetDismissed) },
        sheetState = rememberModalBottomSheetState()
    ) {
        SingleTopAppBar(
            title = stringResource(R.string.search_sort_title),
            leadingIcon = X,
            onLeadingClick = { onIntent(SearchUiIntent.SheetDismissed) }
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Theme.space.large, vertical = Theme.space.medium),
            verticalArrangement = Arrangement.spacedBy(Theme.space.medium)
        ) {
            SelectableChip(
                caption = stringResource(R.string.search_sort_price_low_high),
                selected = selected == ProductSort.PRICE_ASC,
                unselectedColor = Theme.color.neutralDark,
                onClick = { onIntent(SearchUiIntent.SortSelected(ProductSort.PRICE_ASC)) },
                modifier = Modifier.fillMaxWidth()
            )
            SelectableChip(
                caption = stringResource(R.string.search_sort_price_high_low),
                selected = selected == ProductSort.PRICE_DESC,
                unselectedColor = Theme.color.neutralDark,
                onClick = { onIntent(SearchUiIntent.SortSelected(ProductSort.PRICE_DESC)) },
                modifier = Modifier.fillMaxWidth()
            )
            VerticalSpacerMedium()
        }
    }
}

@Composable
private fun SheetSectionTitle(text: String) {
    LafyuuText(
        text = text,
        style = Theme.typography.heading5,
        color = Theme.color.neutralDark
    )
}

@Composable
private fun SelectableChip(
    caption: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    unselectedColor: Color = Theme.color.neutralGrey
) {
    LabelButton(
        caption = caption,
        onClick = onClick,
        containerColor = if (selected) Theme.color.blue.copy(alpha = 0.1f) else Color.Transparent,
        contentColor = if (selected) Theme.color.blue else unselectedColor,
        style = if (selected) Theme.typography.normalCaptionBold else Theme.typography.normalCaptionRegular,
        hasBorder = true,
        modifier = modifier
    )
}