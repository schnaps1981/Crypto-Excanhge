package repository

import models.CryptoError

interface IDbResponse<T> {
    val result: T?
    val isSuccess: Boolean
    val errors: List<CryptoError>
}
