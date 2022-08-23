package models

import helpers.NONE
import kotlinx.datetime.Instant
import java.math.BigDecimal


data class CryptoOrder(
    var ownerId: CryptoUserId = CryptoUserId.NONE,
    var orderId: CryptoOrderId = CryptoOrderId.NONE,
    var created: Instant = Instant.NONE,
    var orderState: CryptoOrderState = CryptoOrderState.NONE,
    var amount: BigDecimal = ZERO,
    var quantity: BigDecimal = ZERO,
    var price: BigDecimal = ZERO,
    var orderType: CryptoOrderType = CryptoOrderType.NONE,
    var pair: CryptoPair = CryptoPair(),
    var lock: CryptoLock = CryptoLock.NONE
) {
    fun deepCopy() = CryptoOrder(
        ownerId = this@CryptoOrder.ownerId,
        orderId = this@CryptoOrder.orderId,
        created = this@CryptoOrder.created,
        orderState = this@CryptoOrder.orderState,
        amount = this@CryptoOrder.amount,
        quantity = this@CryptoOrder.quantity,
        price = this@CryptoOrder.price,
        orderType = this@CryptoOrder.orderType,
        pair = this@CryptoOrder.pair.deepCopy(),
        lock = this@CryptoOrder.lock
    )

    companion object {
        val ZERO: BigDecimal = BigDecimal.valueOf(0.0)

        val EMPTY = CryptoOrder()
    }
}
