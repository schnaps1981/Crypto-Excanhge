package helpers

import models.CryptoError
import models.CryptoErrorLevels

fun errorValidation(
    field: String,
    /**
     * Код, характеризующий ошибку. Не должен включать имя поля или указание на валидацию.
     * Например: empty, badSymbols, tooLong, etc
     */
    violationCode: String,
    description: String,
    level: CryptoErrorLevels = CryptoErrorLevels.ERROR,
) = CryptoError(
    code = "validation-$field-$violationCode",
    field = field,
    group = "validation",
    message = "Validation error for field $field: $description",
    level = level,
)

fun Throwable.asCryptoError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = CryptoError(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this,
)