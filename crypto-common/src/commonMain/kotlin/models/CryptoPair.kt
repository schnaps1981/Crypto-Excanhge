package models

data class CryptoPair(
    var first: String = "",
    var second: String = ""
) {
    fun isEmpty() = this === EMPTY

    companion object {
        val EMPTY = CryptoPair()
    }
}
