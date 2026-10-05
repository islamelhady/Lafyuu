package com.elhady.lafyuu.feature.profile.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.elhady.lafyuu.core.designsystem.components.appbar.SingleTopAppBar
import com.elhady.lafyuu.core.designsystem.components.element.LafyuuScaffold
import com.elhady.lafyuu.core.designsystem.components.list.SingleListItem
import com.elhady.lafyuu.core.designsystem.components.other.LafyuuSnackBarVisuals
import com.elhady.lafyuu.core.designsystem.icons.Bag
import com.elhady.lafyuu.core.designsystem.icons.CreditCard
import com.elhady.lafyuu.core.designsystem.icons.Location
import com.elhady.lafyuu.core.designsystem.icons.User
import com.elhady.lafyuu.core.designsystem.theme.Theme

@Composable
fun AccountRoute(
    onNavigateToProfile: () -> Unit,
    onNavigateToOrders: () -> Unit,
    onNavigateToAddress: () -> Unit,
    onNavigateToPayment: () -> Unit,
    viewModel: AccountViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                AccountUiEffect.NavigateToProfile -> onNavigateToProfile()
                AccountUiEffect.NavigateToOrders -> onNavigateToOrders()
                AccountUiEffect.NavigateToAddress -> onNavigateToAddress()
                AccountUiEffect.NavigateToPayment -> onNavigateToPayment()
                is AccountUiEffect.ShowSnackbar -> {
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

    AccountScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onEvent = viewModel::onEvent,
    )
}

@Composable
fun AccountScreen(
    uiState: AccountUiState,
    snackbarHostState: SnackbarHostState,
    onEvent: (AccountUiEvent) -> Unit,
) {
    LafyuuScaffold(
        snackbarHostState = snackbarHostState,
        topBar = {
            SingleTopAppBar(
                title = "Account",
                leadingIcon = null,
                onLeadingClick = {}
            )
        }
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Theme.color.backgroundWhite,
                    shape = Theme.corner.small
                )
                .padding(Theme.space.large),
        ) {
            SingleListItem(
                title = "Profile",
                leadingIcon = User,
                leadingIconTint = Theme.color.blue,
                onClick = { onEvent(AccountUiEvent.ProfileClicked) },
            )
            SingleListItem(
                title = "Order",
                leadingIcon = Bag,
                leadingIconTint = Theme.color.blue,
                onClick = { onEvent(AccountUiEvent.OrdersClicked) }
            )
            SingleListItem(
                title = "Address",
                leadingIcon = Location,
                leadingIconTint = Theme.color.blue,
                onClick = { onEvent(AccountUiEvent.AddressClicked) }
            )
            SingleListItem(
                title = "Payment",
                leadingIcon = CreditCard,
                leadingIconTint = Theme.color.blue,
                onClick = { onEvent(AccountUiEvent.PaymentClicked) }
            )
        }
    }
}