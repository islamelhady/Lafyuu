package com.elhady.lafyuu.feature.checkout.domain.usecase

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.checkout.domain.model.Address
import com.elhady.lafyuu.feature.checkout.domain.repository.CheckoutRepository
import javax.inject.Inject

class UpdateAddressUseCase @Inject constructor(
    private val repository: CheckoutRepository
) {
    suspend operator fun invoke(
        addressId: String,
        state: String,
        city: String,
        street: String,
        apartment: String,
        phoneNumber: String,
        notes: String
    ): AppResult<Address> {
        if (addressId.isBlank()) {
            return AppResult.Error("Invalid address ID")
        }
        if (state.isBlank() || city.isBlank() || street.isBlank() || phoneNumber.isBlank()) {
            return AppResult.Error("Please Fill The Form")
        }
        return repository.updateAddress(addressId, state, city, street, apartment, phoneNumber, notes)
    }
}
