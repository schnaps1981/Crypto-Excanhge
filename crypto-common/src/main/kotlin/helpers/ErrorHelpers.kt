package helpers

import models.CryptoError
import models.CryptoErrorLevels
import repository.DbOrderResponse

fun errorConcurrency(
    violationCode: String,
    description: String,
    level: CryptoErrorLevels = CryptoErrorLevels.ERROR,
) = CryptoError(
    field = "lock",
    code = "concurrent-$violationCode",
    group = "concurrency",
    message = "Concurrent object access error: $description",
    level = level,
)

val resultErrorIdNotFound = DbOrderResponse(
    result = null,
    isSuccess = false,
    errors = listOf(
        CryptoError(
            field = "id",
            message = "Not Found"
        )
    )
)

val resultErrorIdIsEmpty = DbOrderResponse(
    result = null,
    isSuccess = false,
    errors = listOf(
        CryptoError(
            field = "id",
            message = "Id must not be null or blank"
        )
    )
)

val resultErrorConcurrent = DbOrderResponse(
    result = null,
    isSuccess = false,
    errors = listOf(
        errorConcurrency(
            violationCode = "changed",
            description = "Object has changed during request handling"
        ),
    )
)

fun errorAdministration(
    /**
     * Код, характеризующий ошибку. Не должен включать имя поля или указание на валидацию.
     * Например: empty, badSymbols, tooLong, etc
     */
    field: String = "",
    violationCode: String,
    description: String,
    level: CryptoErrorLevels = CryptoErrorLevels.ERROR,
) = CryptoError(
    field = field,
    code = "administration-$violationCode",
    group = "administration",
    message = "Microservice management error: $description",
    level = level,
)
