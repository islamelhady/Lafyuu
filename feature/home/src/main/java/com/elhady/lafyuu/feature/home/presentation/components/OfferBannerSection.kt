package com.elhady.lafyuu.feature.home.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.elhady.lafyuu.core.designsystem.components.card.Banner
import com.elhady.lafyuu.core.designsystem.components.other.SlideShowIndicator
import com.elhady.lafyuu.core.designsystem.theme.Theme
import com.elhady.lafyuu.feature.home.presentation.model.OfferUiModel
import kotlinx.coroutines.delay

private const val AUTO_SCROLL_DELAY_MS = 3000L

@Composable
fun OfferBannerSection(
    offers: List<OfferUiModel>,
    modifier: Modifier = Modifier
) {
    if (offers.isEmpty()) return

    val pagerState = rememberPagerState(pageCount = { offers.size })
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(pagerState, offers.size, lifecycleOwner) {
        if (offers.size > 1) {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                while (true) {
                    delay(AUTO_SCROLL_DELAY_MS)
                    if (!pagerState.isScrollInProgress) {
                        pagerState.animateScrollToPage((pagerState.currentPage + 1) % offers.size)
                    }
                }
            }
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Theme.space.medium),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState,
            key = { offers[it].id },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Theme.space.large)
        ) { page ->
            val offer = offers[page]
            Banner(
                title = offer.title,
                subtitle = offer.subtitle,
                imageUrl = offer.imageUrl
            )
        }

        if (offers.size > 1) {
            SlideShowIndicator(pageCount = offers.size, currentPage = pagerState.currentPage)
        }
    }
}