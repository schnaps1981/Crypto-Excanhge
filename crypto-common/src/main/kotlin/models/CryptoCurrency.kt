package models

import java.math.BigDecimal

data class CryptoCurrency(
    var ticker: String = "",
    var value: BigDecimal = BigDecimal.valueOf(0.0)
)
