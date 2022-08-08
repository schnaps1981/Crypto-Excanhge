package repository

import models.CryptoError
import models.CryptoOrder

data class DbOrdersResponse(
    override val result: List<CryptoOrder>? = null,
    override val isSuccess: Boolean = false,
    override val errors: List<CryptoError> = emptyList()
) : IDbResponse<List<CryptoOrder>> {
    fun isEmpty() = this === EMPTY

    companion object {
        val EMPTY = DbOrdersResponse()

        fun withErrorMessage(message: String) = this.EMPTY.copy(
            errors = listOf(CryptoError(message = message))
        )
    }
}
