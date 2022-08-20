package models.filter

import helpers.NONE
import kotlinx.datetime.Instant

data class CryptoFilterByDate(
    val orderDate: Instant = Instant.NONE
) : ICryptoFilter {
    override fun deepCopy() = CryptoFilterByDate(
        orderDate = this@CryptoFilterByDate.orderDate
    )
}
