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
class ValidateCorrect {
    private val settings by lazy {
        CryptoSettings(
            repoTest = OrderRepositoryInMemory()
        )
    }

    private val processor = CryptoOrderProcessor(settings)

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

        assertEquals(order, context.orderValidated)
    }
}
