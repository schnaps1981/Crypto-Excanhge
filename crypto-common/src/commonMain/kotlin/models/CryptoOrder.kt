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
) {
    fun deepCopy() = CryptoOrder(
        orderId = this@CryptoOrder.orderId,
        created = this@CryptoOrder.created,
        orderState = this@CryptoOrder.orderState,
        amount = this@CryptoOrder.amount,
        quantity = this@CryptoOrder.quantity,
        price = this@CryptoOrder.price,
        orderType = this@CryptoOrder.orderType,
        pair = this@CryptoOrder.pair.deepCopy()
    )
}
