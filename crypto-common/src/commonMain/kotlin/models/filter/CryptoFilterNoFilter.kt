package models.filter

data class CryptoFilterNoFilter(
    val stub: String = ""
) : ICryptoFilter {
    override fun deepCopy() = CryptoFilterNoFilter(
        stub = this@CryptoFilterNoFilter.stub
    )
}
