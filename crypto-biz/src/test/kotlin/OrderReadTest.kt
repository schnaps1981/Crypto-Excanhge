import context.CryptoOrderContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import models.CryptoOrder
import models.CryptoRequestId
import models.CryptoState
import models.CryptoWorkMode
import models.commands.CryptoOrderCommands
import org.junit.Test
import processors.CryptoOrderProcessor
import stubs.CryptoOrderStubs
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class OrderReadTest {
    private val processor = CryptoOrderProcessor()

    @Test
    fun `stub success read order  test`() = runTest {
        val context = CryptoOrderContext(
            command = CryptoOrderCommands.READ,
            state = CryptoState.NONE,
            workMode = CryptoWorkMode.STUB,
            stubCase = CryptoOrderStubs.SUCCESS
        )

        processor.exec(context)
        println(context.ordersResponse)

        assertEquals(OrderStubs.listOfOrders, context.ordersResponse)
    }

    @Test
    fun `stub failed read order test`() = runTest {
        val context = CryptoOrderContext(
            command = CryptoOrderCommands.READ,
            state = CryptoState.NONE,
            workMode = CryptoWorkMode.STUB,
            stubCase = CryptoOrderStubs.FAILED,
            requestId = CryptoRequestId("123-321")
        )

        processor.exec(context)
        println(context.errors)

        assertEquals("fail", context.errors.firstOrNull()?.code)
        assertEquals("read", context.errors.firstOrNull()?.group)
    }

    @Test
    fun `stub not found read order test`() = runTest {
        val context = CryptoOrderContext(
            command = CryptoOrderCommands.READ,
            state = CryptoState.NONE,
            workMode = CryptoWorkMode.STUB,
            stubCase = CryptoOrderStubs.NOT_FOUND
        )

        processor.exec(context)
        println(context.errors)

        assertEquals("notFound", context.errors.firstOrNull()?.code)
        assertEquals("read", context.errors.firstOrNull()?.group)
    }

    @Test
    fun `stub bad filter read order test`() = runTest {
        val context = CryptoOrderContext(
            command = CryptoOrderCommands.READ,
            state = CryptoState.NONE,
            workMode = CryptoWorkMode.STUB,
            stubCase = CryptoOrderStubs.BAD_FILTER
        )

        processor.exec(context)
        println(context.errors)

        assertEquals("badFilter", context.errors.firstOrNull()?.code)
        assertEquals("validation", context.errors.firstOrNull()?.group)
    }

    @Test
    fun `stub no case read order test`() = runTest {
        val context = CryptoOrderContext(
            command = CryptoOrderCommands.READ,
            state = CryptoState.NONE,
            workMode = CryptoWorkMode.STUB,
            stubCase = CryptoOrderStubs.CANNOT_DELETE
        )

        processor.exec(context)
        println(context.errors)

        assertEquals(CryptoOrder(), context.orderResponse)
        assertEquals("stub", context.errors.firstOrNull()?.field)
        assertEquals("validation", context.errors.firstOrNull()?.group)
    }
}
