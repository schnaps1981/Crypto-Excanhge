package crypto.app.inmemory.model

import helpers.NONE
import kotlinx.datetime.Instant
import models.*
import java.math.BigDecimal

data class OrderEntity(
    val ownerId: String? = null,
    val orderId: String? = null,
    val created: String? = null,
    val orderState: String? = null,
    val amount: String? = null,
    val quantity: String? = null,
    val price: String? = null,
    val orderType: String? = null,
    val pair: PairEntity? = null,
    val lock: String? = null
) {

    constructor(model: CryptoOrder) : this(
        ownerId = model.ownerId.asString().takeIf { it.isNotBlank() },
        orderId = model.orderId.asString().takeIf { it.isNotBlank() },
        created = model.created.toString().takeIf { it.isNotBlank() },
        orderState = model.orderState.takeIf { it != CryptoOrderState.NONE }?.name,
        amount = model.amount.toStringOrNull(),
        quantity = model.quantity.toStringOrNull(),
        price = model.price.toStringOrNull(),
        orderType = model.orderType.takeIf { it != CryptoOrderType.NONE }?.name,
        pair = model.pair.toEntity(),
        lock = model.lock.asString().takeIf { it.isNotBlank() }
    )

    fun toInternal() = CryptoOrder(
        ownerId = ownerId?.let { CryptoUserId(it) } ?: CryptoUserId.NONE,
        orderId = orderId?.let { CryptoOrderId(it) } ?: CryptoOrderId.NONE,
        created = created?.let { Instant.parse(it) } ?: Instant.NONE,
        orderState = orderState?.let { CryptoOrderState.valueOf(it) } ?: CryptoOrderState.NONE,
        amount = amount?.let { BigDecimal(it) } ?: CryptoOrder.ZERO,
        quantity = quantity?.let { BigDecimal(it) } ?: CryptoOrder.ZERO,
        price = price?.let { BigDecimal(it) } ?: CryptoOrder.ZERO,
        orderType = orderType?.let { CryptoOrderType.valueOf(it) } ?: CryptoOrderType.NONE,
        pair = pair?.toInternal() ?: CryptoPair.EMPTY,
        lock = lock?.let { CryptoLock(it) } ?: CryptoLock.NONE,
    )
}

fun BigDecimal.toStringOrNull(): String? = if (this != CryptoOrder.ZERO) {
    this.toString()
} else {
    null
}
