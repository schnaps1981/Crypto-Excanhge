package validation

import context.CryptoOrderContext
import crypto.app.inmemory.OrderRepositoryInMemory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import models.*
import models.commands.CryptoOrderCommands
import org.junit.Test
import processors.CryptoOrderProcessor
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class ValidateQuantity {
    private val settings by lazy {
        CryptoSettings(
            repoTest = OrderRepositoryInMemory()
        )
    }

    private val processor = CryptoOrderProcessor(settings)

    @Test
    fun `validate zero quantity`() = runTest {
        val context = CryptoOrderContext(
            command = CryptoOrderCommands.CREATE,
            state = CryptoState.NONE,
            workMode = CryptoWorkMode.TEST,
            orderRequest = CryptoOrder(
                orderType = CryptoOrderType.BUY,
                amount = 1.0.toBigDecimal(),
                pair = CryptoPair("BTC", "USD"),
                price = 1.0.toBigDecimal(),
            )
        )

        processor.exec(context)

        println(context.errors)

        assertEquals(1, context.errors.size)
        assertEquals(CryptoState.FAILED, context.state)

        val error = context.errors.firstOrNull()
        assertEquals("validation", error?.group)
        assertEquals("quantity", error?.field)
        assertEquals("validation-quantity-zero", error?.code)

        assertEquals(context.orderValidated, CryptoOrder())
    }
}
