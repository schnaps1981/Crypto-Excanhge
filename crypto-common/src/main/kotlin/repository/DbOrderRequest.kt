package repository

import models.CryptoOrder

data class DbOrderRequest(
    val order: CryptoOrder
)