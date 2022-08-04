package models

data class CryptoPair(
    var first: String = "",
    var second: String = ""
) {
    fun isEmpty() = this === EMPTY

    fun deepCopy() = CryptoPair(
        first = this@CryptoPair.first,
        second = this@CryptoPair.second
    )

    companion object {
        val EMPTY = CryptoPair()
    }
}
