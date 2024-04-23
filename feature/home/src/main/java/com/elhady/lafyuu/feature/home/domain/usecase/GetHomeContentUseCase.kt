package com.elhady.lafyuu.feature.home.domain.usecase

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

        val categoriesRes = categoriesDeferred.await()
        val offersRes = offersDeferred.await()
        val flashSaleRes = flashSaleDeferred.await()
        val megaSaleRes = megaSaleDeferred.await()
        val recommendedRes = recommendedDeferred.await()

        val categories = (categoriesRes as? AppResult.Success)?.data.orEmpty()
        val offers = (offersRes as? AppResult.Success)?.data.orEmpty()

        val p1 = (flashSaleRes as? AppResult.Success)?.data.orEmpty()
        val p2 = (megaSaleRes as? AppResult.Success)?.data.orEmpty()
        val p3 = (recommendedRes as? AppResult.Success)?.data.orEmpty()

        val allProducts = (p1 + p2 + p3).distinctBy { it.id }

        val flashSaleProducts = p1.ifEmpty { allProducts.take(6) }
        val megaSaleProducts = p2.ifEmpty { allProducts.take(6) }
        val recommendedProducts = p3.ifEmpty { allProducts }

        val hasTotalError = (categoriesRes is AppResult.Error) &&
                (flashSaleRes is AppResult.Error) &&
                (recommendedRes is AppResult.Error)

        if (hasTotalError) {
            val errorMsg = (categoriesRes as AppResult.Error).message
                ?: (flashSaleRes as? AppResult.Error)?.message
                ?: (recommendedRes as? AppResult.Error)?.message
                ?: (offersRes as? AppResult.Error)?.message
                ?: "Failed to load content. Please check your network connection."
            AppResult.Error(message = errorMsg)
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
