package com.elhady.lafyuu.feature.product.data.mapper

import com.elhady.lafyuu.feature.product.data.remote.model.ProductDto
import com.elhady.lafyuu.feature.product.data.remote.model.UserReviewDto
import com.elhady.lafyuu.feature.product.domain.model.Product
import com.elhady.lafyuu.feature.product.domain.model.ProductDetails
import com.elhady.lafyuu.feature.product.domain.model.ProductReview

fun ProductDto.toProductDetailsDomain(): ProductDetails {
    val cover = coverPictureUrl ?: coverUrl ?: imageUrl ?: image ?: productPictures?.firstOrNull() ?: ""
    val picturesList = productPictures?.filter { it.isNotBlank() }
        ?.ifEmpty { if (cover.isNotBlank()) listOf(cover) else emptyList() }
        ?: if (cover.isNotBlank()) listOf(cover) else emptyList()

    val currentPrice = price ?: 0.0
    val discount = discountPercentage?.toInt() ?: 0
    val origPrice = originalPrice ?: if (discount > 0 && currentPrice > 0.0) {
        currentPrice / (1.0 - (discount / 100.0))
    } else null

    val defaultColors = listOf(
        "#FFC833", "#40BFFF", "#FB7181", "#53D1B6", "#5C61F4", "#223263"
    )

    val defaultSizes = listOf("6", "6.5", "7", "7.5", "8", "8.5")

    return ProductDetails(
        id = id.orEmpty(),
        productCode = productCode.orEmpty(),
        name = name ?: title.orEmpty(),
        description = description ?: arabicDescription.orEmpty(),
        coverPictureUrl = cover,
        productPictures = picturesList,
        price = currentPrice,
        originalPrice = origPrice,
        discountPercentage = discount,
        stock = stock ?: 0,
        color = color.orEmpty(),
        availableColorsHex = defaultColors,
        availableSizes = defaultSizes,
        rating = rating?.toInt() ?: 0,
        reviewsCount = reviewsCount ?: 0,
        sellerId = sellerId.orEmpty()
    )
}

fun ProductDto.toProductSummaryDomain(): Product {
    val cover = coverPictureUrl ?: coverUrl ?: imageUrl ?: image ?: productPictures?.firstOrNull()
    val currentPrice = price ?: 0.0
    val discount = discountPercentage?.toInt() ?: 0
    val origPrice = originalPrice ?: if (discount > 0 && currentPrice > 0.0) {
        currentPrice / (1.0 - (discount / 100.0))
    } else null

    return Product(
        id = id.orEmpty(),
        name = name ?: title.orEmpty(),
        price = currentPrice,
        originalPrice = origPrice,
        discountPercentage = discount,
        rating = rating?.toInt() ?: 0,
        imageUrl = cover
    )
}

fun UserReviewDto.toDomain(): ProductReview {
    return ProductReview(
        comment = comment.orEmpty(),
        rating = rating?.toInt() ?: 0,
        createdAt = createdAt.orEmpty(),
        userName = userName.orEmpty(),
        userPicture = userPicture
    )
}
