import context.CryptoOrderContext
import kotlinx.datetime.Instant
import models.*
import models.commands.CryptoOrderCommands
import org.junit.Test
import kotlin.test.assertEquals

class MapperTest {

    @Test
    fun fromContext() {
        val context = CryptoOrderContext(
            requestId = CryptoRequestId("1234"),
            command = CryptoOrderCommands.CREATE,
            ordersResponse = mutableListOf(
                CryptoOrder(
                    ownerId = CryptoUserId("234"),
                    orderId = CryptoOrderId("order-id"),
                    created = Instant.fromEpochMilliseconds(1231313131),
                    orderState = CryptoOrderState.ACTIVE,
                    amount = 3.3.toBigDecimal(),
                    quantity = 4.4.toBigDecimal(),
                    price = 5.5.toBigDecimal(),
                    orderType = CryptoOrderType.SELL,
                    pair = CryptoPair("BTC", "USD")
                )
            ),
            errors = mutableListOf(
                CryptoError(
                    code = "err",
                    group = "request",
                    field = "id",
                    message = "wrong id",
                )
            ),
            state = CryptoState.RUNNING,
        )

        val log = context.toLog("test-id")

        assertEquals("test-id", log.logId)
        assertEquals("crypto", log.source)
        assertEquals("1234", log.order?.requestId)
        assertEquals("order-id", log.order?.responseOrders?.firstOrNull()?.orderId)
        assertEquals("wrong id", log.errors?.firstOrNull()?.message)
    }
}