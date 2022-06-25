import context.CryptoOrderContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import models.*
import models.commands.CryptoOrderCommands
import org.junit.Test
import processors.CryptoOrderProcessor
import stubs.CryptoOrderStubs
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class OrderCreateTest {

    private val processor = CryptoOrderProcessor()

    @Test
    fun `stub success create order test`() = runTest {
        val context = CryptoOrderContext(
            command = CryptoOrderCommands.CREATE,
            state = CryptoState.NONE,
            workMode = CryptoWorkMode.STUB,
            stubCase = CryptoOrderStubs.SUCCESS
        )

        processor.exec(context)
        println(context.orderResponse.orderId.asString())
        println(context.orderResponse.amount)

        assertEquals(context.orderResponse.amount, OrderStubs.oneOrderStub.amount)
        assertEquals(context.orderResponse.orderId.asString(), OrderStubs.oneOrderStub.orderId.asString())
    }

    @Test
    fun `stub failed create order test`() = runTest {
        val context = CryptoOrderContext(
            command = CryptoOrderCommands.CREATE,
            state = CryptoState.NONE,
            workMode = CryptoWorkMode.STUB,
            stubCase = CryptoOrderStubs.FAILED,
            requestId = CryptoRequestId("123321")
        )

        processor.exec(context)
        println(context.errors)

        assertEquals("create", context.errors.firstOrNull()?.group)
        assertEquals("fail", context.errors.firstOrNull()?.code)
    }

    @Test
    fun `stub no case test`() = runTest {
        val ctx = CryptoOrderContext(
            command = CryptoOrderCommands.CREATE,
            state = CryptoState.NONE,
            workMode = CryptoWorkMode.STUB,
            stubCase = CryptoOrderStubs.CANNOT_DELETE,
            orderRequest = CryptoOrder(
                orderId = CryptoOrderId("123"),

                )
        )
        processor.exec(ctx)

        println(ctx.errors)

        assertEquals(CryptoOrder(), ctx.orderResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
}
