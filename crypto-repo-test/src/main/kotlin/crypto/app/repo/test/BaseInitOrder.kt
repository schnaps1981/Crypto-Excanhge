package crypto.app.repo.test

import models.*

abstract class BaseInitOrder(private val operation: String) : IInitObjects<CryptoOrder> {

    fun createInitTestModel(
        ownerId: CryptoUserId = CryptoUserId("test-owner-123"),
        orderType: CryptoOrderType = CryptoOrderType.BUY,
        orderState: CryptoOrderState = CryptoOrderState.ACTIVE,
        orderPair: CryptoPair = CryptoPair("BTC", "USD"),
        orderDate: String = "0"//TODO пределать в INSTANT
    ) = CryptoOrder(
        //TODO добавить в модель CryptoOrder поле ownerId = ownerId
        orderId = CryptoOrderId("crypto-reo-$operation"),
        created = System.currentTimeMillis().toInt(), //TODO пределать в INSTANT
        orderState = orderState,
        amount = 1.0.toBigDecimal(),
        quantity = 2.0.toBigDecimal(),
        price = 100.0.toBigDecimal(),
        orderType = orderType,
        pair = orderPair
    )
}
