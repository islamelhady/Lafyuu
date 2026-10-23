package com.elhady.lafyuu.feature.home.domain.usecase

import com.elhady.lafyuu.core.common.result.AppError
import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.home.domain.model.HomeContent
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
        val flashSaleDeferred = async { homeRepository.getProducts(page = 1, pageSize = 10) }
        val megaSaleDeferred = async { homeRepository.getProducts(page = 2, pageSize = 10) }
        val recommendedDeferred = async { homeRepository.getProducts(page = 1, pageSize = 20) }

        val categoriesResult = categoriesDeferred.await()
        val offersResult = offersDeferred.await()
        val flashSaleResult = flashSaleDeferred.await()
        val megaSaleResult = megaSaleDeferred.await()
        val recommendedResult = recommendedDeferred.await()

        val categories = (categoriesResult as? AppResult.Success)?.data.orEmpty()
        val offers = (offersResult as? AppResult.Success)?.data.orEmpty()

        val flashSaleItems = (flashSaleResult as? AppResult.Success)?.data.orEmpty()
        val megaSaleItems = (megaSaleResult as? AppResult.Success)?.data.orEmpty()
        val recommendedItems = (recommendedResult as? AppResult.Success)?.data.orEmpty()

        val allProducts = (flashSaleItems + megaSaleItems + recommendedItems).distinctBy { it.id }

        val flashSaleProducts = flashSaleItems.ifEmpty { allProducts.take(6) }
        val megaSaleProducts = megaSaleItems.ifEmpty { allProducts.take(6) }
        val recommendedProducts = recommendedItems.ifEmpty { allProducts }

        val hasTotalError = (categoriesResult is AppResult.Failure) &&
                (flashSaleResult is AppResult.Failure) &&
                (recommendedResult is AppResult.Failure)

        if (hasTotalError) {
            val errorMsg = (categoriesResult as? AppResult.Failure)?.error
                ?: (flashSaleResult as? AppResult.Failure)?.error
                ?: (recommendedResult as? AppResult.Failure)?.error
                ?: (offersResult as? AppResult.Failure)?.error
                ?: AppError.Unknown
            AppResult.Failure(error = errorMsg)
        } else {
            AppResult.Success(
                HomeContent(
                    categories = categories,
                    offers = offers,
                    flashSaleProducts = flashSaleProducts,
                    megaSaleProducts = megaSaleProducts,
                    recommendedProducts = recommendedProducts
                )
            )
        }
    }
}
