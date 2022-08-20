package biz.order

import context.CryptoOrderContext
import crypto.app.inmemory.OrderRepositoryInMemory
import helpers.NONE
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import models.*
import models.commands.CryptoOrderCommands
import org.junit.Test
import processors.CryptoOrderProcessor
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RepoOrderCreateTest {
    private val command = CryptoOrderCommands.CREATE

    private val settings by lazy {
        CryptoSettings(
            repoTest = OrderRepositoryInMemory()
        )
    }

    private val processor = CryptoOrderProcessor(settings)

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun createSuccessTest() = runTest {
        val tradePair = CryptoPair(first = "BTC", second = "USD")

        val context = CryptoOrderContext(
            command = command,
            state = CryptoState.NONE,
            workMode = CryptoWorkMode.TEST,
            orderRequest = CryptoOrder(
                ownerId = CryptoUserId("user-123"),
                quantity = "1.4".toBigDecimal(),
                price = "100.3".toBigDecimal(),
                amount = "1".toBigDecimal(),
                orderType = CryptoOrderType.SELL,
                pair = tradePair
            )
        )

        processor.exec(context)

        println(context)

        assertEquals(CryptoState.FINISHING, context.state)
        assertTrue(context.orderResponse.orderId != CryptoOrderId.NONE)
        assertTrue(context.orderResponse.orderId != CryptoOrderId.NONE)
        assertTrue(context.orderResponse.created != Instant.NONE)
        assertEquals(context.orderResponse.orderState, CryptoOrderState.ACTIVE)
        assertEquals(context.orderResponse.amount, "1".toBigDecimal())
        assertEquals(context.orderResponse.quantity, "1.4".toBigDecimal())
        assertEquals(context.orderResponse.price, "100.3".toBigDecimal())
        assertEquals(context.orderResponse.pair, tradePair)
        assertEquals(context.errors, emptyList())
    }
}
