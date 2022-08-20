package models.filter

import models.CryptoOrderState

data class CryptoFilterByState(
    val orderState: CryptoOrderState = CryptoOrderState.NONE
) : ICryptoFilter {
    override fun deepCopy() = CryptoFilterByState(
        orderState = this@CryptoFilterByState.orderState
    )
}
