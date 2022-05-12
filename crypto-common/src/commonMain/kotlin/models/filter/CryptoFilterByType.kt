package models.filter

import models.CryptoOrderType

data class CryptoFilterByType(
    var orderType: CryptoOrderType = CryptoOrderType.NONE
) : ICryptoFilter
