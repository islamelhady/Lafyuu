package com.elhady.lafyuu.feature.offers.domain.usecase

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.offers.domain.model.Offer
import com.elhady.lafyuu.feature.offers.domain.repository.OffersRepository
import javax.inject.Inject

class GetOffersUseCase @Inject constructor(
    private val repository: OffersRepository
) {
    suspend operator fun invoke(): AppResult<List<Offer>> {
        return repository.getOffers()
    }
}
