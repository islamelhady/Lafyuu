package com.elhady.lafyuu.feature.home.domain.usecase

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.home.domain.model.HomeContent
import com.elhady.lafyuu.feature.home.domain.model.HomeSection
import com.elhady.lafyuu.feature.home.domain.model.PRODUCTS_POOL_SIZE
import com.elhady.lafyuu.feature.home.domain.model.ProductQuery
import com.elhady.lafyuu.feature.home.domain.model.toSaleSections
import com.elhady.lafyuu.feature.home.domain.repository.HomeRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class GetHomeContentUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    suspend operator fun invoke(): AppResult<HomeContent> = coroutineScope {
        val categoriesDeferred = async { homeRepository.getCategories() }
        val offersDeferred = async { homeRepository.getOffers() }
        val productsDeferred = async { homeRepository.getProducts(ProductQuery(pageSize = PRODUCTS_POOL_SIZE)) }

        val categoriesResult = categoriesDeferred.await()
        val offersResult = offersDeferred.await()
        val productsResult = productsDeferred.await()

        val failures = listOf(categoriesResult, offersResult, productsResult)
            .filterIsInstance<AppResult.Failure>()
        if (failures.size == 3) return@coroutineScope failures.first()

        val failedSections = buildSet {
            if (categoriesResult is AppResult.Failure) add(HomeSection.Categories)
            if (offersResult is AppResult.Failure) add(HomeSection.Offers)
            if (productsResult is AppResult.Failure) {
                add(HomeSection.FlashSale)
                add(HomeSection.MegaSale)
                add(HomeSection.Recommended)
            }
        }

        val products = (productsResult as? AppResult.Success)?.data.orEmpty()
        val saleSections = products.toSaleSections()

        AppResult.Success(
            HomeContent(
                categories = (categoriesResult as? AppResult.Success)?.data.orEmpty(),
                offers = (offersResult as? AppResult.Success)?.data.orEmpty(),
                flashSaleProducts = saleSections.flash,
                megaSaleProducts = saleSections.mega,
                recommendedProducts = products,
                failedSections = failedSections
            )
        )
    }
}