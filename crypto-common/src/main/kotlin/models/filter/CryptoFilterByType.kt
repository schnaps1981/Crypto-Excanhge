package models.filter

import models.CryptoFilterApplyTo
import models.CryptoOrderType

data class CryptoFilterByType(
    var orderType: CryptoOrderType = CryptoOrderType.NONE,
    override var filterPermissions: MutableSet<CryptoFilterApplyTo> = mutableSetOf()
) : ICryptoFilter {
    override fun deepCopy() = CryptoFilterByType(
        orderType = this@CryptoFilterByType.orderType
    )
}
