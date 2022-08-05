package crypto.app.repo.test

import helpers.NONE
import kotlinx.datetime.Instant
import models.*
import java.util.*

abstract class BaseInitOrder(private val operation: String) : IInitObjects<CryptoOrder> {

    fun createInitTestModel(
        ownerId: CryptoUserId = CryptoUserId("test-owner-123"),
        orderType: CryptoOrderType = CryptoOrderType.BUY,
        orderState: CryptoOrderState = CryptoOrderState.ACTIVE,
        orderPair: CryptoPair = CryptoPair("RUB", "ETH"),
        orderDate: Instant = Instant.NONE,
        orderId: String = ""
    ) = CryptoOrder(
        orderId = CryptoOrderId(
            orderId.ifBlank { "$operation-${UUID.randomUUID()}" }
        ),
        ownerId = ownerId,
        created = orderDate,
        orderState = orderState,
        amount = 1.0.toBigDecimal(),
        quantity = 2.0.toBigDecimal(),
        price = 100.0.toBigDecimal(),
        orderType = orderType,
        pair = orderPair
    )
}
