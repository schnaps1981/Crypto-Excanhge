package validation

import context.CryptoOrderContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import models.*
import models.commands.CryptoOrderCommands
import org.junit.Test
import processors.CryptoOrderProcessor
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@ExperimentalCoroutinesApi
class ValidateCorrect {
    private val processor = CryptoOrderProcessor()

    @Test
    fun `validate correct order`() = runTest {
        val order = CryptoOrder(
            quantity = 1.0.toBigDecimal(),
            orderType = CryptoOrderType.BUY,
            amount = 1.0.toBigDecimal(),
            pair = CryptoPair("BTC", "USD"),
            price = 1.0.toBigDecimal(),
        )

        val context = CryptoOrderContext(
            command = CryptoOrderCommands.CREATE,
            state = CryptoState.NONE,
            workMode = CryptoWorkMode.TEST,
            orderRequest = order
        )

        processor.exec(context)

        println(context)

        assertEquals(0, context.errors.size)
        assertNotEquals(CryptoState.FINISHING, context.state)

        assertEquals(order, context.orderValidated)
    }
}
