package validation.filter

import context.CryptoOrderContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import models.*
import models.commands.CryptoOrderCommands
import models.filter.CryptoFilterByCurrency
import models.filter.ICryptoFilterNone
import org.junit.Test
import processors.CryptoOrderProcessor
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@ExperimentalCoroutinesApi
class ValidateFilterByCurrency {
    private val processor = CryptoOrderProcessor()

    @Test
    fun `validate filter by currency`() = runTest {
        val context = CryptoOrderContext(
            command = CryptoOrderCommands.READ,
            state = CryptoState.NONE,
            workMode = CryptoWorkMode.TEST,
            orderFilter = CryptoFilterByCurrency(
                ticker = "BTC"
            )
        )

        processor.exec(context)

        println(context)

        assertEquals(0, context.errors.size)
        assertNotEquals(CryptoState.FAILED, context.state)

        assertEquals("BTC", (context.orderFilterValidated as CryptoFilterByCurrency).ticker )
    }

    @Test
    fun `validate filter by currency empty`() = runTest {
        val context = CryptoOrderContext(
            command = CryptoOrderCommands.READ,
            state = CryptoState.NONE,
            workMode = CryptoWorkMode.TEST,
            orderFilter = CryptoFilterByCurrency(
                ticker = ""
            )
        )

        processor.exec(context)

        println(context.errors)

        assertEquals(1, context.errors.size)
        assertEquals(CryptoState.FAILED, context.state)

        val error = context.errors.firstOrNull()
        assertEquals("validation", error?.group)
        assertEquals("filter", error?.field)
        assertEquals("validation-filter-ticker", error?.code)

        assertEquals(context.orderFilterValidated,  ICryptoFilterNone)
    }


}
