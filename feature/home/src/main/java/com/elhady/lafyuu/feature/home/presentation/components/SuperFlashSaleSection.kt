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
import com.elhady.lafyuu.core.designsystem.theme.LafyuuTheme
import com.elhady.lafyuu.core.designsystem.theme.Theme
import com.elhady.lafyuu.feature.home.domain.model.Product
import kotlinx.coroutines.delay
import java.util.Locale

@Composable
fun SuperFlashSaleSection(
    products: List<Product>,
    onProductClick: (productId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = products.take(5)
    if (items.isEmpty()) return

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
            val product = items[page]
            val title = product.name.ifBlank { "Super Flash Sale" }
            val subtitle = product.discountLabel ?: String.format(Locale.US, "$%.2f", product.price)

            Banner(
                title = title,
                subtitle = subtitle,
                imageUrl = product.coverPictureUrl,
                onClick = {
                    if (product.id.isNotBlank()) {
                        onProductClick(product.id)
                    }
                }
            )
        }

        if (items.size > 1) {
            SlideShowIndicator(
                pageCount = items.size,
                currentPage = pagerState.currentPage
            )
        }
    }
}

@Preview
@Composable
fun SuperFlashSaleSectionPreview() {
    LafyuuTheme {
        SuperFlashSaleSection(
            products = listOf(
                Product(
                    id = "1",
                    name = "FS - Nike Air Max 270",
                    coverPictureUrl = null,
                    price = 299.43,
                    originalPrice = 399.0,
                    discountPercentage = 25.0,
                    discountLabel = "24% OFF",
                    rating = 4
                )
            ),
            onProductClick = {}
        )
    }
}
