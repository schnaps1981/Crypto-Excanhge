package models.filter

data class CryptoFilterByDate(
    val orderDate: String = ""
) : ICryptoFilter {
    override fun deepCopy() = CryptoFilterByDate(
        orderDate = this@CryptoFilterByDate.orderDate
    )
}
