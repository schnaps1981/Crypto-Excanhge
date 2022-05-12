package models.filter

import models.CryptoOrderState

data class CryptoFilterByState(
    val orderState: CryptoOrderState = CryptoOrderState.NONE
) : ICryptoFilter
