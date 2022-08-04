package models

@JvmInline
value class CryptoRequestId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = CryptoRequestId("")
    }
}

@JvmInline
value class CryptoUserId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = CryptoUserId("")
    }
}

@JvmInline
value class CryptoOrderId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = CryptoOrderId("")
    }
}
