package models

import java.math.BigDecimal

data class CryptoOrder(
    var orderId: CryptoOrderId = CryptoOrderId.NONE,
    var created: Int = 0,
    var orderState: CryptoOrderState = CryptoOrderState.NONE,
    var amount: BigDecimal = BigDecimal.valueOf(0.0),
    var quantity: BigDecimal = BigDecimal.valueOf(0.0),
    var price: BigDecimal = BigDecimal.valueOf(0.0),
    var orderType: CryptoOrderType = CryptoOrderType.NONE,
    var pair: CryptoPair = CryptoPair()
)
