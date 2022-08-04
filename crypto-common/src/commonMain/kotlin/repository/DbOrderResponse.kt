package repository

import models.CryptoError
import models.CryptoOrder

data class DbOrderResponse(
    override val result: CryptoOrder?,
    override val isSuccess: Boolean,
    override val errors: List<CryptoError> = emptyList()
): IDbResponse<CryptoOrder>
