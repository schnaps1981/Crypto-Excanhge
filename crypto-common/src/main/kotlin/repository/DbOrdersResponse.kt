package repository

import models.CryptoError
import models.CryptoOrder

data class DbOrdersResponse(
    override val result: List<CryptoOrder>?,
    override val isSuccess: Boolean,
    override val errors: List<CryptoError> = emptyList()
): IDbResponse<List<CryptoOrder>>
