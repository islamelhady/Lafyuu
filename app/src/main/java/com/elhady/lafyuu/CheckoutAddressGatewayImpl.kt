package com.elhady.lafyuu

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.common.map
import com.elhady.lafyuu.feature.address.domain.model.Address
import com.elhady.lafyuu.feature.address.domain.usecase.DeleteAddressUseCase
import com.elhady.lafyuu.feature.address.domain.usecase.GetAddressesUseCase
import com.elhady.lafyuu.feature.cart.domain.repository.CartRepository
import com.elhady.lafyuu.feature.checkout.domain.model.OrderSummary
import com.elhady.lafyuu.feature.checkout.domain.model.ShippingAddress
import com.elhady.lafyuu.feature.checkout.domain.repository.CheckoutAddressGateway
import com.elhady.lafyuu.feature.checkout.domain.repository.CheckoutCartGateway
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CheckoutAddressGatewayImpl @Inject constructor(
    private val getAddressesUseCase: GetAddressesUseCase,
    private val deleteAddressUseCase: DeleteAddressUseCase
) : CheckoutAddressGateway {

    override suspend fun getAddresses(): AppResult<List<ShippingAddress>> =
        getAddressesUseCase().map { addresses -> addresses.map { it.toShippingAddress() } }

    override suspend fun deleteAddress(addressId: String): AppResult<Unit> =
        deleteAddressUseCase(addressId)
}

class CheckoutCartGatewayImpl @Inject constructor(
    private val cartRepository: CartRepository
) : CheckoutCartGateway {

    override suspend fun getOrderSummary(): AppResult<OrderSummary> {
        // The cached cart still knows the applied coupon, a fresh GET /api/cart doesn't return it
        val cart = cartRepository.observeCart().first()
            ?: when (val result = cartRepository.getCart()) {
                is AppResult.Success -> result.data
                is AppResult.Failure -> return result
            }
        return AppResult.Success(
            OrderSummary(
                total = cart.finalTotal ?: cart.itemsTotal,
                couponCode = cart.appliedCoupon?.code
            )
        )
    }

    override suspend fun refreshCart() {
        cartRepository.getCart()
    }
}

private fun Address.toShippingAddress() = ShippingAddress(
    id = id,
    state = state,
    city = city,
    street = street,
    apartment = apartment,
    phoneNumber = phoneNumber
)
