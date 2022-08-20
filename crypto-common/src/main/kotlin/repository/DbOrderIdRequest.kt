package repository

import models.CryptoLock
import models.CryptoOrderId

data class DbOrderIdRequest(
    val id: CryptoOrderId,
    val lock: CryptoLock = CryptoLock.NONE
)
