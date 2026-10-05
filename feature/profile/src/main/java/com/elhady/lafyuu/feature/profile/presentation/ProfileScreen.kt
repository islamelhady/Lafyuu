package com.elhady.lafyuu.feature.profile.presentation

import Left
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.elhady.lafyuu.core.designsystem.components.appbar.SingleTopAppBar
import com.elhady.lafyuu.core.designsystem.components.element.LafyuuScaffold
import com.elhady.lafyuu.core.designsystem.components.list.SingleListItem
import com.elhady.lafyuu.core.designsystem.components.other.LafyuuSnackBarVisuals
import com.elhady.lafyuu.core.designsystem.components.text.LafyuuText
import com.elhady.lafyuu.core.designsystem.icons.Password
import com.elhady.lafyuu.core.designsystem.icons.Trash
import com.elhady.lafyuu.core.designsystem.icons.User
import com.elhady.lafyuu.core.designsystem.theme.Theme

@Composable
fun ProfileRoute(
    onNavigateToChangePassword: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                ProfileUiEffect.NavigateToChangePassword -> onNavigateToChangePassword()
                ProfileUiEffect.NavigateToLogin -> onNavigateToLogin()
                ProfileUiEffect.NavigateBack -> onNavigateBack()
                is ProfileUiEffect.ShowSnackbar -> {
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

    ProfileScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onEvent = viewModel::onEvent,
    )
}

@Composable
fun ProfileScreen(
    uiState: ProfileUiState,
    snackbarHostState: SnackbarHostState,
    onEvent: (ProfileUiEvent) -> Unit,
) {
    LafyuuScaffold(
        snackbarHostState = snackbarHostState,
        topBar = {
            SingleTopAppBar(
                title = "Profile",
                leadingIcon = Left,
                onLeadingClick = { onEvent(ProfileUiEvent.BackClicked)}
            )
        }
    ) {
        when {
            uiState.isLoading && uiState.userProfile == null -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Theme.color.blue
                )
            }

            else -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(Theme.space.large)
                ) {
                    uiState.userProfile?.let { profile ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Theme.color.backgroundWhite, shape = Theme.corner.small)
                                .padding(Theme.space.large),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(Theme.space.medium)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(CircleShape)
                                    .background(Theme.color.blue.copy(alpha = 0.1f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = User,
                                    contentDescription = null,
                                    tint = Theme.color.blue,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                LafyuuText(
                                    text = profile.fullName,
                                    style = Theme.typography.heading5,
                                    color = Theme.color.neutralDark
                                )
                                LafyuuText(
                                    text = profile.email,
                                    style = Theme.typography.normalTextRegular,
                                    color = Theme.color.neutralGrey
                                )
                            }
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Theme.color.backgroundWhite, shape = Theme.corner.small)
                            .padding(Theme.space.large),
                        verticalArrangement = Arrangement.spacedBy(Theme.space.large)
                    ) {
                        SingleListItem(
                            title = "Change Password",
                            subtitle = "***********",
                            showChevron = true,
                            onClick = { onEvent(ProfileUiEvent.ChangePasswordClicked) },
                            leadingIcon = Password,
                            leadingIconTint = Theme.color.blue
                        )
                        SingleListItem(
                            title = "Logout",
                            subtitle = "Logout",
                            showChevron = true,
                            onClick = { onEvent(ProfileUiEvent.LogoutClicked) },
                            leadingIcon = Icons.AutoMirrored.Filled.Logout,
                            leadingIconTint = Theme.color.error
                        )
                    }
                }
            }
        }
    }
}