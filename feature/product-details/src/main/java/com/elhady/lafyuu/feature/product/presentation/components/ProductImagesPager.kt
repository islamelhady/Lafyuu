package com.elhady.lafyuu.feature.product.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.elhady.lafyuu.core.designsystem.R
import com.elhady.lafyuu.core.designsystem.components.other.SlideShowIndicator
import com.elhady.lafyuu.core.designsystem.theme.Theme
import com.elhady.lafyuu.feature.product.presentation.model.ProductDetailsUi

@Composable
fun ProductImagesPager(product: ProductDetailsUi) {
    if (product.pictureUrls.isEmpty()) return
    val pagerState = rememberPagerState(pageCount = { product.pictureUrls.size })
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Theme.space.medium)
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(Theme.size.productImageHeight)
        ) { page ->
            AsyncImage(
                model = product.pictureUrls[page],
                contentDescription = product.name,
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.imp_product_shoes_yellow),
                error = painterResource(R.drawable.imp_product_shoes_yellow),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = Theme.space.large)
            )
        }
        if (product.pictureUrls.size > 1) {
            Spacer(modifier = Modifier.height(Theme.space.large))
            SlideShowIndicator(
                pageCount = product.pictureUrls.size,
                currentPage = pagerState.currentPage
            )
        }
    }
}