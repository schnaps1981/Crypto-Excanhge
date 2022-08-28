package models.filter

import models.CryptoFilterApplyTo
import models.CryptoOrderState

data class CryptoFilterByState(
    val orderState: CryptoOrderState = CryptoOrderState.NONE,
    override var filterPermissions: MutableSet<CryptoFilterApplyTo> = mutableSetOf()
) : ICryptoFilter {
    override fun deepCopy() = CryptoFilterByState(
        orderState = this@CryptoFilterByState.orderState
    )
}
