package models

import java.math.BigDecimal

data class CryptoPairRate(
    var pair: CryptoPair = CryptoPair(),
    var rate: BigDecimal = BigDecimal.valueOf(0.0)
)
