package com.elhady.lafyuu.feature.checkout.domain.usecase

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.checkout.domain.model.Address
import com.elhady.lafyuu.feature.checkout.domain.repository.CheckoutRepository
import javax.inject.Inject

class CreateAddressUseCase @Inject constructor(
    private val repository: CheckoutRepository
) {
    suspend operator fun invoke(
        state: String,
        city: String,
        street: String,
        apartment: String,
        phoneNumber: String,
        notes: String
    ): AppResult<Address> {
        if (state.isBlank() || city.isBlank() || street.isBlank() || phoneNumber.isBlank()) {
            return AppResult.Error("Please Fill The Form")
        }
        val addressesResult = repository.getAddresses()
        if (addressesResult is AppResult.Success) {
            if (addressesResult.data.size >= 5) {
                return AppResult.Error("Maximum limit of 5 addresses reached")
            }
        }
        return repository.createAddress(state, city, street, apartment, phoneNumber, notes)
    }
}
