package com.elhady.lafyuu.feature.offers.data.repository

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.network.error.SafeApiCaller
import com.elhady.lafyuu.feature.offers.data.mapper.toDomain
import com.elhady.lafyuu.feature.offers.data.remote.OffersApi
import com.elhady.lafyuu.feature.offers.domain.model.Offer
import com.elhady.lafyuu.feature.offers.domain.repository.OffersRepository
import javax.inject.Inject

class OffersRepositoryImpl @Inject constructor(
    private val offersApi: OffersApi,
    private val safeApiCaller: SafeApiCaller
) : OffersRepository {

    override suspend fun getOffers(): AppResult<List<Offer>> {
        return safeApiCaller.call { offersApi.getOffers().items.orEmpty().map { it.toDomain() } } }
}
