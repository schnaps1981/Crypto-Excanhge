package helpers

import models.CryptoError
import models.CryptoErrorLevels

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
