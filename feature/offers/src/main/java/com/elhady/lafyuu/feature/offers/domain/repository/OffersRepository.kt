package com.elhady.lafyuu.feature.offers.domain.repository

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.offers.domain.model.Offer

interface OffersRepository {
    suspend fun getOffers(): AppResult<List<Offer>>
}
