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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import coil.compose.AsyncImage
import com.elhady.lafyuu.core.designsystem.R
import com.elhady.lafyuu.core.designsystem.components.card.ProductCard
import com.elhady.lafyuu.core.designsystem.components.element.ColorSelectRow
import com.elhady.lafyuu.core.designsystem.components.element.ReviewCard
import com.elhady.lafyuu.core.designsystem.components.element.ReviewData
import com.elhady.lafyuu.core.designsystem.components.other.RatingBar
import com.elhady.lafyuu.core.designsystem.components.other.SelectingGroup
import com.elhady.lafyuu.core.designsystem.components.other.SlideShowIndicator
import com.elhady.lafyuu.core.designsystem.components.text.LafyuuText
import com.elhady.lafyuu.core.designsystem.components.text.SectionTitle
import com.elhady.lafyuu.core.designsystem.icons.Love
import com.elhady.lafyuu.core.designsystem.icons.LoveFilled
import com.elhady.lafyuu.core.designsystem.theme.Theme
import com.elhady.lafyuu.feature.product.presentation.ProductDetailsUiEvent
import com.elhady.lafyuu.feature.product.presentation.ProductDetailsUiState
import kotlin.collections.ifEmpty
import kotlin.collections.isNotEmpty

@Composable
fun ProductDetailsContent(
    uiState: ProductDetailsUiState,
    onEvent: (ProductDetailsUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val product = uiState.product ?: return
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .verticalScroll(scrollState)
            .padding(bottom = Theme.space.medium)
    ) {
        // Image Carousel
        val pictures = product.productPictures.ifEmpty {
            listOfNotNull(product.coverPictureUrl.takeIf { it.isNotBlank() })
        }
        if (pictures.isNotEmpty()) {
            val pagerState = rememberPagerState(pageCount = { pictures.size })
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
                        .height(238.dp)
                ) { page ->
                    AsyncImage(
                        model = pictures[page],
                        contentDescription = product.name,
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(R.drawable.imp_product_shoes_yellow),
                        error = painterResource(R.drawable.imp_product_shoes_yellow),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = Theme.space.large)
                    )
                }
                if (pictures.size > 1) {
                    Spacer(modifier = Modifier.height(Theme.space.large))
                    SlideShowIndicator(
                        pageCount = pictures.size,
                        currentPage = pagerState.currentPage
                    )
                }
            }
        }

        Column(
            modifier = Modifier.padding(horizontal = Theme.space.large),
            verticalArrangement = Arrangement.spacedBy(Theme.space.large)
        ) {
            // Title & Favorite Header
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
                IconButton(
                    onClick = { onEvent(ProductDetailsUiEvent.FavoriteToggled) }
                ) {
                    Icon(
                        imageVector = if (uiState.isFavorite) LoveFilled else Love,
                        contentDescription = "Favorite",
                        tint = if (uiState.isFavorite) Theme.color.red else Theme.color.neutralGrey
                    )
                }
            }

            // Rating
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Theme.space.medium)
            ) {
                RatingBar(
                    rating = product.rating,
                    iconSize = Theme.size.iconSmall
                )
                LafyuuText(
                    text = product.rating.toString(),
                    style = Theme.typography.heading5,
                    color = Theme.color.neutralGrey
                )
            }

            // Price
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Theme.space.medium)
            ) {
                LafyuuText(
                    text = "$${"%.2f".format(product.price)}",
                    style = Theme.typography.heading3,
                    color = Theme.color.blue
                )
                product.originalPrice?.let { orig ->
                    LafyuuText(
                        text = "$${"%.2f".format(orig)}",
                        style = Theme.typography.normalTextRegular.copy(
                            textDecoration = TextDecoration.LineThrough
                        ),
                        color = Theme.color.neutralGrey
                    )
                }
                if (product.discountPercentage > 0) {
                    LafyuuText(
                        text = "${product.discountPercentage}% Off",
                        style = Theme.typography.normalTextBold,
                        color = Theme.color.red
                    )
                }
            }

            // Select Size
            if (product.availableSizes.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(Theme.space.medium)) {
                    SectionTitle("Select Size")
                    SelectingGroup(
                        options = product.availableSizes,
                        selectedOption = uiState.selectedSize,
                        onOptionSelected = { onEvent(ProductDetailsUiEvent.SizeSelected(it)) }
                    )
                }
            }

            // Select Color
            if (product.availableColorsHex.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(Theme.space.medium)) {
                    SectionTitle("Select Color")
                    val parsedColors = remember(product.availableColorsHex) {
                        product.availableColorsHex.mapNotNull { hex ->
                            try {
                                Color(hex.toColorInt())
                            } catch (_: Exception) {
                                null
                            }
                        }
                    }
                    val currentSelectedColor = remember(uiState.selectedColorHex) {
                        try {
                            Color(uiState.selectedColorHex.toColorInt())
                        } catch (_: Exception) {
                            parsedColors.firstOrNull() ?: Color.Yellow
                        }
                    }
                    ColorSelectRow(
                        colors = parsedColors,
                        selectedColor = currentSelectedColor,
                        onColorSelected = { color ->
                            val hexString = "#%02x%02x%02x".format(
                                (color.red * 255).toInt(),
                                (color.green * 255).toInt(),
                                (color.blue * 255).toInt()
                            )
                            onEvent(ProductDetailsUiEvent.ColorSelected(hexString))
                        }
                    )
                }
            }

            // Specifications & Description
            Column(verticalArrangement = Arrangement.spacedBy(Theme.space.medium)) {
                SectionTitle("Specification")
                if (product.productCode.isNotBlank()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        LafyuuText(
                            text = "Style:",
                            style = Theme.typography.normalTextRegular,
                            color = Theme.color.neutralGrey
                        )
                        LafyuuText(
                            text = product.productCode,
                            style = Theme.typography.normalTextRegular,
                            color = Theme.color.neutralDark
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    LafyuuText(
                        text = "Stock:",
                        style = Theme.typography.normalTextRegular,
                        color = Theme.color.neutralGrey
                    )
                    LafyuuText(
                        text = "${product.stock} items",
                        style = Theme.typography.normalTextRegular,
                        color = Theme.color.neutralDark
                    )
                }
                if (product.description.isNotBlank()) {
                    LafyuuText(
                        text = product.description,
                        style = Theme.typography.normalTextRegular,
                        color = Theme.color.neutralGrey,
                        modifier = Modifier.padding(top = Theme.space.small)
                    )
                }
            }

            // Reviews
            if (uiState.reviews.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(Theme.space.medium)) {
                    SectionTitle("Review Product")
                    uiState.reviews.take(2).forEach { review ->
                        ReviewCard(
                            review = ReviewData(
                                userName = review.userName,
                                avatarUrl = R.drawable.img_profile_man_bearded,
                                rating = review.rating,
                                comment = review.comment,
                                date = review.createdAt
                            )
                        )
                    }
                }
            }

            // You Might Also Like
            if (uiState.recommendedProducts.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(Theme.space.medium)) {
                    SectionTitle("You Might Also Like")
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(Theme.space.large),
                        contentPadding = PaddingValues(vertical = Theme.space.small)
                    ) {
                        items(
                            items = uiState.recommendedProducts,
                            key = { it.id }
                        ) { recProduct ->
                            ProductCard(
                                imageUrl = recProduct.imageUrl,
                                name = recProduct.name,
                                price = "$${"%.2f".format(recProduct.price)}",
                                originalPrice = recProduct.originalPrice?.let { "$${"%.2f".format(it)}" },
                                discountLabel = if (recProduct.discountPercentage > 0) "${recProduct.discountPercentage}% OFF" else null,
                                rating = recProduct.rating,
                                onClick = {
                                    onEvent(
                                        ProductDetailsUiEvent.RecommendedProductClicked(
                                            recProduct.id
                                        )
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}