package com.elhady.lafyuu.feature.offers.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import com.elhady.lafyuu.core.designsystem.components.card.Banner
import com.elhady.lafyuu.core.designsystem.components.card.InformationCard
import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.core.designsystem.components.element.InfoStateContent
import com.elhady.lafyuu.core.designsystem.components.element.LafyuuScaffold
import com.elhady.lafyuu.core.designsystem.components.element.NavigationBottomBar
import com.elhady.lafyuu.core.designsystem.components.element.defaultTabBarItems
import com.elhady.lafyuu.core.designsystem.components.other.LafyuuSnackBarVisuals
import com.elhady.lafyuu.core.designsystem.theme.Theme

@Composable
fun OffersRoute(
    onNavigateToTab: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: OffersViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is OffersUiEffect.NavigateToTab -> onNavigateToTab(effect.tabRoute)
                is OffersUiEffect.ShowSnackbar -> {
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

    OffersScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onEvent = viewModel::onEvent,
        modifier = modifier
    )
}

@Composable
fun OffersScreen(
    uiState: OffersUiState,
    snackbarHostState: SnackbarHostState,
    onEvent: (OffersUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    LafyuuScaffold(
        snackbarHostState = snackbarHostState,
        topBar = {
            SingleTopAppBar(
                title = "Offer",
                onLeadingClick = {}
            )
        },
        bottomBar = {
            NavigationBottomBar(
                tabs = defaultTabBarItems,
                selectedTab = "offer",
                onTabSelected = { onEvent(OffersUiEvent.BottomTabSelected(it)) }
            )
        }
    ) {
        when {
            uiState.isLoading && uiState.offers.isEmpty() -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Theme.color.blue
                )
            }

            uiState.offers.isEmpty() -> {
                InfoStateContent(
                    errorMessage = "No offers available",
                    onRetryClick = { onEvent(OffersUiEvent.RetryClicked) },
                    errorType = AlertType.Warning,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = Theme.space.large),
                    verticalArrangement = Arrangement.spacedBy(Theme.space.large)
                ) {
                    item {
                        InformationCard(
                            information = "Use \"MEGSL\" Coupon For \nGet 90% off"
                        )
                    }
                    items(uiState.offers, key = { it.id }) { offer ->
                        Banner(
                            title = offer.name,
                            subtitle = offer.description ?: "50% Off",
                            imageUrl = offer.coverUrl,
                            onClick = { onEvent(OffersUiEvent.OfferClicked(offer)) }
                        )
                    }
                }
            }
        }
    }
}
