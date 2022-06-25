package models.filter

data class CryptoFilterByCurrency(
    val ticker: String = ""
) : ICryptoFilter {
    override fun deepCopy() = CryptoFilterByCurrency(
        ticker = this@CryptoFilterByCurrency.ticker
    )
}
