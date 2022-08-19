package models.filter

import models.CryptoFilterApplyTo

data class CryptoFilterNoFilter(
    val stub: String = "",
    override var filterPermissions: MutableSet<CryptoFilterApplyTo> = mutableSetOf()
) : ICryptoFilter {
    override fun deepCopy() = CryptoFilterNoFilter(
        stub = this@CryptoFilterNoFilter.stub
    )
}
