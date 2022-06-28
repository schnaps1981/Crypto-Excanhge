package models

enum class CryptoErrorLevels {
    ERROR,
    INFO,
}

data class CryptoError(
    val code: String = "",
    val group: String = "",
    val field: String = "",
    val message: String = "",
    val level: CryptoErrorLevels = CryptoErrorLevels.ERROR,
    val exception: Throwable? = null
)
