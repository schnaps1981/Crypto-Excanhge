package models.filter

data class CryptoFilterByCurrency(
    val ticker: String = ""
) : ICryptoFilter
