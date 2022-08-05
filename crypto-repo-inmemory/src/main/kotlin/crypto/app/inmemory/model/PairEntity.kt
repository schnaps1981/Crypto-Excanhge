package crypto.app.inmemory.model

import models.CryptoPair

data class PairEntity(
    val first: String? = null,
    val second: String? = null
) {
    constructor(model: CryptoPair) : this(
        first = model.first.takeIf { it.isNotBlank() },
        second = model.second.takeIf { it.isNotBlank() }
    )

    fun toInternal() = CryptoPair(
        first = first ?: "",
        second = second ?: ""
    )
}

internal fun CryptoPair.toEntity() = when {
    first.isNotBlank() && second.isNotBlank() -> {
        PairEntity(this)
    }

    this.isEmpty() -> null

    else -> null
}
