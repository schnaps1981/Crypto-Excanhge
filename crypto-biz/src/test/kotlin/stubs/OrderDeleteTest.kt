package stubs

import context.CryptoOrderContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import models.*
import models.commands.CryptoOrderCommands
import org.junit.Test
import processors.CryptoOrderProcessor
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class OrderDeleteTest {
    private val processor = CryptoOrderProcessor()

    @Test
    fun `stub success delete order test`() = runTest {
        val context = CryptoOrderContext(
            command = CryptoOrderCommands.DELETE,
            state = CryptoState.NONE,
            workMode = CryptoWorkMode.STUB,
            stubCase = CryptoOrderStubs.SUCCESS,
            orderRequest = CryptoOrder(
                orderId = CryptoOrderId("123321")
            )
        )

        processor.exec(context)
        println(context.orderResponse.orderState)
        println(context.orderResponse.orderId.asString())

        assertEquals(CryptoOrderState.NONE, context.orderResponse.orderState)
        assertEquals("123321", context.orderResponse.orderId.asString())
    }

    @Test
    fun `stub failed delete order test`() = runTest {
        val context = CryptoOrderContext(
            command = CryptoOrderCommands.DELETE,
            state = CryptoState.NONE,
            workMode = CryptoWorkMode.STUB,
            stubCase = CryptoOrderStubs.FAILED,
            orderRequest = CryptoOrder(
                orderId = CryptoOrderId("123321")
            )
        )

        processor.exec(context)
        println(context.errors)

        assertEquals("fail", context.errors.firstOrNull()?.code)
        assertEquals("delete", context.errors.firstOrNull()?.group)
    }

    @Test
    fun `stub cannot delete order test`() = runTest {
        val context = CryptoOrderContext(
            command = CryptoOrderCommands.DELETE,
            state = CryptoState.NONE,
            workMode = CryptoWorkMode.STUB,
            stubCase = CryptoOrderStubs.CANNOT_DELETE,
            orderRequest = CryptoOrder(
                orderId = CryptoOrderId("123321")
            )
        )

        processor.exec(context)
        println(context.errors)

        assertEquals("cant", context.errors.firstOrNull()?.code)
        assertEquals("delete", context.errors.firstOrNull()?.group)
    }

    @Test
    fun `stub validation bad id delete order test`() = runTest {
        val context = CryptoOrderContext(
            command = CryptoOrderCommands.DELETE,
            state = CryptoState.NONE,
            workMode = CryptoWorkMode.STUB,
            stubCase = CryptoOrderStubs.BAD_ID,
            orderRequest = CryptoOrder(
                orderId = CryptoOrderId("123321")
            )
        )

        processor.exec(context)
        println(context.errors)

        assertEquals("validation", context.errors.firstOrNull()?.group)
        assertEquals("CryptoOrderId", context.errors.firstOrNull()?.field)
    }

    @Test
    fun `stub no case delete order test`() = runTest {
        val ctx = CryptoOrderContext(
            command = CryptoOrderCommands.DELETE,
            state = CryptoState.NONE,
            workMode = CryptoWorkMode.STUB,
            stubCase = CryptoOrderStubs.BAD_FILTER,
            orderRequest = CryptoOrder(
                orderId = CryptoOrderId("123")
            )
        )
        processor.exec(ctx)

        println(ctx.errors)

        assertEquals(CryptoOrder(), ctx.orderResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
}
