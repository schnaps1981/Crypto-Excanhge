package models

@JvmInline
value class CryptoLock(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = CryptoLock("")
    }
}
