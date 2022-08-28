package models.filter

import helpers.NONE
import kotlinx.datetime.Instant
import models.CryptoFilterApplyTo

data class CryptoFilterByDate(
    val orderDate: Instant = Instant.NONE,
    override var filterPermissions: MutableSet<CryptoFilterApplyTo> = mutableSetOf()
) : ICryptoFilter {
    override fun deepCopy() = CryptoFilterByDate(
        orderDate = this@CryptoFilterByDate.orderDate
    )
}
