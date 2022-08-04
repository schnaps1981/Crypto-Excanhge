package crypto.app.repo.test

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import models.*
import java.util.*

abstract class BaseInitOrder(private val operation: String) : IInitObjects<CryptoOrder> {

    fun createInitTestModel(
        ownerId: CryptoUserId = CryptoUserId("test-owner-123"),
        orderType: CryptoOrderType = CryptoOrderType.BUY,
        orderState: CryptoOrderState = CryptoOrderState.ACTIVE,
        orderPair: CryptoPair = CryptoPair("BTC", "USD"),
        orderDate: Instant = Clock.System.now(),
        orderId: CryptoOrderId = CryptoOrderId("${UUID.randomUUID()}")
    ) = CryptoOrder(
        ownerId = ownerId,
        orderId = orderId,
        created = orderDate,
        orderState = orderState,
        amount = 1.0.toBigDecimal(),
        quantity = 2.0.toBigDecimal(),
        price = 100.0.toBigDecimal(),
        orderType = orderType,
        pair = orderPair
    )
}
