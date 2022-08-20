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
class ValidateOrderId {
    private val settings by lazy {
        CryptoSettings(
            repoTest = OrderRepositoryInMemory()
        )
    }

    private val processor = CryptoOrderProcessor(settings)

    @Test
    fun `validate order id empty`() = runTest {
        val context = CryptoOrderContext(
            command = CryptoOrderCommands.DELETE,
            state = CryptoState.NONE,
            workMode = CryptoWorkMode.TEST
        )

        processor.exec(context)

        println(context.errors)

        assertEquals(1, context.errors.size)
        assertEquals(CryptoState.FAILED, context.state)

        val error = context.errors.firstOrNull()
        assertEquals("validation", error?.group)
        assertEquals("orderId", error?.field)
        assertEquals("validation-orderId-empty", error?.code)

        assertEquals(context.orderValidated, CryptoOrder())
    }

    @Test
    fun `validate order id not empty`() = runTest {
        val context = CryptoOrderContext(
            command = CryptoOrderCommands.DELETE,
            state = CryptoState.NONE,
            workMode = CryptoWorkMode.TEST,
            orderRequest = CryptoOrder(orderId = CryptoOrderId("123321"))
        )

        processor.exec(context)

        println(context)

        assertEquals(1, context.errors.size)
        assertEquals(CryptoState.FAILED, context.state)

        assertEquals(context.orderValidated.orderId, CryptoOrderId("123321"))
    }

}
