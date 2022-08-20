package validation.filter

import context.CryptoOrderContext
import crypto.app.inmemory.OrderRepositoryInMemory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import models.*
import models.commands.CryptoOrderCommands
import models.filter.*
import org.junit.Test
import processors.CryptoOrderProcessor
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@ExperimentalCoroutinesApi
class ValidateFilterByType {

    private val settings by lazy {
        CryptoSettings(
            repoTest = OrderRepositoryInMemory()
        )
    }

    private val processor = CryptoOrderProcessor(settings)

    @Test
    fun `validate filter by state`() = runTest {
        val context = CryptoOrderContext(
            command = CryptoOrderCommands.READ,
            state = CryptoState.NONE,
            workMode = CryptoWorkMode.TEST,
            orderFilter = CryptoFilterByType(
                orderType = CryptoOrderType.BUY
            )
        )

        processor.exec(context)

        println(context)

        assertEquals(0, context.errors.size)
        assertNotEquals(CryptoState.FAILED, context.state)

        assertEquals(CryptoOrderType.BUY, (context.orderFilterValidated as CryptoFilterByType).orderType )
    }

    @Test
    fun `validate filter by date empty`() = runTest {
        val context = CryptoOrderContext(
            command = CryptoOrderCommands.READ,
            state = CryptoState.NONE,
            workMode = CryptoWorkMode.TEST,
            orderFilter = CryptoFilterByType(
                orderType = CryptoOrderType.NONE
            )
        )

        processor.exec(context)

        println(context.errors)

        assertEquals(1, context.errors.size)
        assertEquals(CryptoState.FAILED, context.state)

        val error = context.errors.firstOrNull()
        assertEquals("validation", error?.group)
        assertEquals("filter", error?.field)
        assertEquals("validation-filter-type", error?.code)

        assertEquals(context.orderFilterValidated,  ICryptoFilterNone)
    }


}
