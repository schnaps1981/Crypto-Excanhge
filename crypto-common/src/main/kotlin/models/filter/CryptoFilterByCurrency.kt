package models.filter

import models.CryptoFilterApplyTo

data class CryptoFilterByCurrency(
    val ticker: String = "",
    override var filterPermissions: MutableSet<CryptoFilterApplyTo> = mutableSetOf()
) : ICryptoFilter {
    override fun deepCopy() = CryptoFilterByCurrency(
        ticker = this@CryptoFilterByCurrency.ticker
    )
}
