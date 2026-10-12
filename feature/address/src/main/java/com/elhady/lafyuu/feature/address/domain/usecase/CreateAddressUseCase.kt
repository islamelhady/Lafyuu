package com.elhady.lafyuu.feature.address.domain.usecase

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.address.domain.model.Address
import com.elhady.lafyuu.feature.address.domain.repository.AddressRepository
import javax.inject.Inject

class CreateAddressUseCase @Inject constructor(
    private val repository: AddressRepository
) {
    suspend operator fun invoke(
        state: String,
        city: String,
        street: String,
        apartment: String,
        phoneNumber: String,
        notes: String
    ): AppResult<Address> {
        if (state.isBlank() || city.isBlank() || street.isBlank() || apartment.isBlank() || phoneNumber.isBlank()) {
            return AppResult.Error("Please fill all required address fields")
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
