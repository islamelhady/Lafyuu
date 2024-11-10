package com.elhady.lafyuu.feature.product.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import coil.compose.AsyncImage
import com.elhady.lafyuu.core.designsystem.components.button.LabelButton
import com.elhady.lafyuu.core.designsystem.components.card.InformationCard
import com.elhady.lafyuu.core.designsystem.components.card.ProductCard
import com.elhady.lafyuu.core.designsystem.components.element.ReviewCard
import com.elhady.lafyuu.core.designsystem.components.other.RatingBar
import com.elhady.lafyuu.core.designsystem.components.other.SlideShowIndicator
import com.elhady.lafyuu.core.designsystem.components.text.LafyuuText
import com.elhady.lafyuu.core.designsystem.components.text.SectionTitle
import com.elhady.lafyuu.core.designsystem.icons.Love
import com.elhady.lafyuu.core.designsystem.icons.LoveFilled
import com.elhady.lafyuu.core.designsystem.theme.Theme
import com.elhady.lafyuu.feature.product.R
import com.elhady.lafyuu.feature.product.presentation.ProductDetailsUiIntent
import com.elhady.lafyuu.feature.product.presentation.ProductDetailsUiState
import com.elhady.lafyuu.feature.product.presentation.model.ProductDetailsUi
import com.elhady.lafyuu.core.designsystem.R as DsR

private const val REVIEWS_PREVIEW_COUNT = 2

@Composable
fun ProductDetailsContent(
    uiState: ProductDetailsUiState,
    onIntent: (ProductDetailsUiIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    val product = uiState.product ?: return

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(bottom = Theme.space.medium)
    ) {
        ProductImagesPager(product)

        Column(
            modifier = Modifier.padding(horizontal = Theme.space.large),
            verticalArrangement = Arrangement.spacedBy(Theme.space.large)
        ) {
            ProductHeader(product, uiState.isFavorite, onIntent)
            RatingBar(
                rating = product.rating,
                iconSize = Theme.size.iconSmall,
                showRatingText = true,
                showReviewNumber = product.reviewsCount.toString()
            )
            PriceRow(product)
            SpecificationSection(product)
            ReviewsSection(uiState, onIntent)
            RecommendedSection(uiState, onIntent)
        }
    }
}

@Composable
private fun ProductImagesPager(product: ProductDetailsUi) {
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
                placeholder = painterResource(DsR.drawable.imp_product_shoes_yellow),
                error = painterResource(DsR.drawable.imp_product_shoes_yellow),
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

@Composable
private fun ProductHeader(
    product: ProductDetailsUi,
    isFavorite: Boolean,
    onIntent: (ProductDetailsUiIntent) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        LafyuuText(
            text = product.name,
            style = Theme.typography.heading3,
            color = Theme.color.neutralDark,
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = { onIntent(ProductDetailsUiIntent.FavoriteToggled) }) {
            Icon(
                imageVector = if (isFavorite) LoveFilled else Love,
                contentDescription = stringResource(R.string.product_favorite_cd),
                tint = if (isFavorite) Theme.color.red else Theme.color.neutralGrey
            )
        }
    }
}

@Composable
private fun PriceRow(product: ProductDetailsUi) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Theme.space.medium)
    ) {
        LafyuuText(
            text = product.price,
            style = Theme.typography.heading3,
            color = Theme.color.blue
        )
        product.originalPrice?.let { originalPrice ->
            LafyuuText(
                text = originalPrice,
                style = Theme.typography.normalTextRegular.copy(
                    textDecoration = TextDecoration.LineThrough
                ),
                color = Theme.color.neutralGrey
            )
        }
        if (product.discountPercentage > 0) {
            LafyuuText(
                text = stringResource(R.string.product_discount_off, product.discountPercentage),
                style = Theme.typography.normalTextBold,
                color = Theme.color.red
            )
        }
    }
}

@Composable
private fun SpecificationSection(product: ProductDetailsUi) {
    Column(verticalArrangement = Arrangement.spacedBy(Theme.space.medium)) {
        SectionTitle(stringResource(R.string.product_specification))
        if (product.productCode.isNotBlank()) {
            SpecificationRow(stringResource(R.string.product_style_label), product.productCode)
        }
        SpecificationRow(
            label = stringResource(R.string.product_stock_label),
            value = stringResource(R.string.product_stock_items, product.stock)
        )
        if (product.description.isNotBlank()) {
            LafyuuText(
                text = product.description,
                style = Theme.typography.normalTextRegular,
                color = Theme.color.neutralGrey,
                modifier = Modifier.padding(top = Theme.space.small)
            )
        }
    }
}

@Composable
private fun SpecificationRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        LafyuuText(
            text = label,
            style = Theme.typography.normalTextRegular,
            color = Theme.color.neutralGrey
        )
        LafyuuText(
            text = value,
            style = Theme.typography.normalTextRegular,
            color = Theme.color.neutralDark
        )
    }
}

@Composable
private fun ReviewsSection(
    uiState: ProductDetailsUiState,
    onIntent: (ProductDetailsUiIntent) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(Theme.space.medium)) {
        if (uiState.reviews.isNotEmpty()) {
            SectionTitle(
                title = stringResource(R.string.product_reviews_title),
                actionText = stringResource(R.string.product_see_more),
                onActionClick = { onIntent(ProductDetailsUiIntent.SeeMoreReviewsClicked) }
            )
            uiState.reviews.take(REVIEWS_PREVIEW_COUNT).forEach { review ->
                ReviewCard(
                    userName = review.userName,
                    avatarUrl = painterResource(id = review.userPicture.toIntOrNull() ?: DsR.drawable.img_profile_boy),
                    rating = review.rating,
                    comment = review.comment,
                    date = review.date
                )
            }
            WriteReviewButton(R.string.product_write_review, onIntent)
        } else {
            SectionTitle(title = stringResource(R.string.product_reviews_title))
            InformationCard(information = stringResource(R.string.product_no_reviews))
            WriteReviewButton(R.string.product_write_first_review, onIntent)
        }
    }
}

@Composable
private fun WriteReviewButton(
    captionRes: Int,
    onIntent: (ProductDetailsUiIntent) -> Unit
) {
    LabelButton(
        caption = stringResource(captionRes),
        onClick = { onIntent(ProductDetailsUiIntent.WriteReviewClicked) },
        contentColor = Theme.color.blue,
        hasBorder = true,
        borderColor = Theme.color.blue,
        style = Theme.typography.largeCaptionBold,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = Theme.space.small)
    )
}

@Composable
private fun RecommendedSection(
    uiState: ProductDetailsUiState,
    onIntent: (ProductDetailsUiIntent) -> Unit
) {
    if (uiState.recommendedProducts.isEmpty()) return
    Column(verticalArrangement = Arrangement.spacedBy(Theme.space.medium)) {
        SectionTitle(stringResource(R.string.product_you_might_also_like))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(Theme.space.large),
            contentPadding = PaddingValues(vertical = Theme.space.small)
        ) {
            items(items = uiState.recommendedProducts, key = { it.id }) { item ->
                ProductCard(
                    imageUrl = item.imageUrl,
                    name = item.name,
                    price = item.price,
                    originalPrice = item.originalPrice,
                    discountLabel = if (item.discountPercentage > 0) {
                        stringResource(R.string.product_discount_label, item.discountPercentage)
                    } else null,
                    rating = item.rating,
                    onClick = {
                        onIntent(ProductDetailsUiIntent.RecommendedProductClicked(item.id))
                    }
                )
            }
        }
    }
}