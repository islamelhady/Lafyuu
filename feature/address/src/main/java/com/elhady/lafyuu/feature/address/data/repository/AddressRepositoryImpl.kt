package com.elhady.lafyuu.feature.address.data.repository

import com.elhady.lafyuu.core.common.result.AppResult
import com.elhady.lafyuu.core.network.error.SafeApiCaller
import com.elhady.lafyuu.feature.address.data.mapper.toDomain
import com.elhady.lafyuu.feature.address.data.remote.AddressApi
import com.elhady.lafyuu.feature.address.data.remote.model.CreateAddressRequestDto
import com.elhady.lafyuu.feature.address.data.remote.model.UpdateAddressRequestDto
import com.elhady.lafyuu.feature.address.domain.model.Address
import com.elhady.lafyuu.feature.address.domain.repository.AddressRepository
import javax.inject.Inject

class AddressRepositoryImpl @Inject constructor(
    private val addressApi: AddressApi,
    private val safeApiCaller: SafeApiCaller
) : AddressRepository {

    override suspend fun getAddresses(): AppResult<List<Address>> {
        return safeApiCaller.call { addressApi.getAddresses().map { it.toDomain() } }
    }

    override suspend fun getAddress(addressId: String): AppResult<Address> {
        return safeApiCaller.call { addressApi.getAddress(addressId).toDomain() }
    }

    override suspend fun createAddress(
        state: String,
        city: String,
        street: String,
        apartment: String,
        phoneNumber: String,
        notes: String
    ): AppResult<Address> {
        return safeApiCaller.call {
            addressApi.createAddress(
            request = CreateAddressRequestDto(
                state = state,
                city = city,
                street = street,
                apartment = apartment,
                phoneNumber = phoneNumber,
                notes = notes
            )
        ).toDomain() }
    }

    override suspend fun updateAddress(
        addressId: String,
        state: String,
        city: String,
        street: String,
        apartment: String,
        phoneNumber: String,
        notes: String
    ): AppResult<Address> {
        return safeApiCaller.call {
            addressApi.updateAddress(
            addressId,
            request = UpdateAddressRequestDto(
                id = addressId,
                state = state,
                city = city,
                street = street,
                apartment = apartment,
                phoneNumber = phoneNumber,
                notes = notes
            )
        ).toDomain() }
    }

    override suspend fun deleteAddress(addressId: String): AppResult<Unit> {
        return safeApiCaller.call { addressApi.deleteAddress(addressId) }
    }
}
