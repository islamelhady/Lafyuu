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
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.elhady.lafyuu.core.designsystem.components.card.Banner
import com.elhady.lafyuu.core.designsystem.components.other.SlideShowIndicator
import com.elhady.lafyuu.core.designsystem.theme.Theme
import com.elhady.lafyuu.feature.home.domain.model.Offer
import kotlinx.coroutines.delay

@Composable
fun SuperFlashSaleSection(
    offers: List<Offer>,
    onOfferClick: (offerId: String) -> Unit,
    onSeeMoreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val items = offers.ifEmpty {
        listOf(
            Offer(
                id = "1",
                name = "Super Flash Sale",
                description = "50% Off",
                coverUrl = null
            ),
            Offer(
                id = "2",
                name = "Mega Sale",
                description = "70% Off",
                coverUrl = null
            ),
            Offer(
                id = "3",
                name = "Super Flash Sale",
                description = "30% Off",
                coverUrl = null
            )
        )
    }

    val pagerState = rememberPagerState(pageCount = { items.size })
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(pagerState, items.size, lifecycleOwner) {
        if (items.size > 1) {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                while (true) {
                    delay(3000L)
                    if (!pagerState.isScrollInProgress) {
                        val nextPage = (pagerState.currentPage + 1) % items.size
                        pagerState.animateScrollToPage(nextPage)
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Theme.space.large)
        ) { page ->
            val offer = items[page]
            val title = offer.name.ifBlank { "Super Flash Sale" }
            val subtitle = offer.description?.ifBlank { "50% Off" } ?: "50% Off"

            Banner(
                title = title,
                subtitle = subtitle,
                imageUrl = offer.coverUrl,
                onClick = {
                    if (offer.id.isNotBlank()) {
                        onOfferClick(offer.id)
                    } else {
                        onSeeMoreClick()
                    }
                }
            )
        }

        SlideShowIndicator(
            pageCount = items.size,
            currentPage = pagerState.currentPage
        )
    }
}

@Preview
@Composable
fun SuperFlashSaleSectionPreview() {
    SuperFlashSaleSection(
        offers = listOf(
            Offer(
                id = "1",
                name = "Super Flash Sale",
                description = "50% Off",
                coverUrl = null
            )
        ),
        onOfferClick = {},
        onSeeMoreClick = {}
    )
}
