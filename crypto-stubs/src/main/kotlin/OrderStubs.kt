import helpers.nowMicros
import kotlinx.datetime.Instant
import models.*

object OrderStubs {

    val oneOrderStub = CryptoOrder(
        orderType = CryptoOrderType.BUY,
        pair = CryptoPair(
            first = "BTC",
            second = "USD"
        ),
        price = 10000.0.toBigDecimal(),
        orderState = CryptoOrderState.ACTIVE,
        quantity = 1.0.toBigDecimal(),
        orderId = CryptoOrderId("orderID-123213"),
        amount = 1.0.toBigDecimal(),
        created = Instant.nowMicros
    )

    val listOfOrders = mutableListOf(
        CryptoOrder(
            orderType = CryptoOrderType.BUY,
            pair = CryptoPair(
                first = "BTC",
                second = "USD"
            ),
            price = 10000.0.toBigDecimal(),
            orderState = CryptoOrderState.ACTIVE,
            quantity = 1.0.toBigDecimal(),
            orderId = CryptoOrderId("orderID-123213"),
            amount = 1.0.toBigDecimal(),
            created = Instant.nowMicros
        ),
        CryptoOrder(
            orderType = CryptoOrderType.SELL,
            pair = CryptoPair(
                first = "BTC",
                second = "USD"
            ),
            price = 20000.0.toBigDecimal(),
            orderState = CryptoOrderState.ACTIVE,
            quantity = 0.5.toBigDecimal(),
            orderId = CryptoOrderId("orderID-55555"),
            amount = 1.0.toBigDecimal(),
            created = Instant.nowMicros
        )
    )

    val deletedOrder = CryptoOrder(
        orderState = CryptoOrderState.NONE
    )
}
