package repository

import models.CryptoError
import models.CryptoOrder

data class DbOrderResponse(
    override val result: CryptoOrder? = null,
    override val isSuccess: Boolean = false,
    override val errors: List<CryptoError> = emptyList()
) : IDbResponse<CryptoOrder> {

    fun isEmpty() = this === EMPTY

    companion object {
        val EMPTY = DbOrderResponse()

        fun withErrorMessage(message: String) = this.EMPTY.copy(
            errors = listOf(CryptoError(message = message))
        )
    }
}
