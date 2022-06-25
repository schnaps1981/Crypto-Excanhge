package validation.filter

import context.CryptoOrderContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import models.CryptoState
import models.CryptoWorkMode
import models.commands.CryptoOrderCommands
import models.filter.CryptoFilterByDate
import models.filter.ICryptoFilterNone
import org.junit.Test
import processors.CryptoOrderProcessor
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@ExperimentalCoroutinesApi
class ValidateFilterByDate {
    private val processor = CryptoOrderProcessor()

    @Test
    fun `validate filter by date`() = runTest {
        val context = CryptoOrderContext(
            command = CryptoOrderCommands.READ,
            state = CryptoState.NONE,
            workMode = CryptoWorkMode.TEST,
            orderFilter = CryptoFilterByDate(
                orderDate = "111"
            )
        )

        processor.exec(context)

        println(context)

        assertEquals(0, context.errors.size)
        assertNotEquals(CryptoState.FAILED, context.state)

        assertEquals("111", (context.orderFilterValidated as CryptoFilterByDate).orderDate)
    }

    @Test
    fun `validate filter by date empty`() = runTest {
        val context = CryptoOrderContext(
            command = CryptoOrderCommands.READ,
            state = CryptoState.NONE,
            workMode = CryptoWorkMode.TEST,
            orderFilter = CryptoFilterByDate(
                orderDate = ""
            )
        )

        processor.exec(context)

        println(context.errors)

        assertEquals(1, context.errors.size)
        assertEquals(CryptoState.FAILED, context.state)

        val error = context.errors.firstOrNull()
        assertEquals("validation", error?.group)
        assertEquals("filter", error?.field)
        assertEquals("validation-filter-date", error?.code)

        assertEquals(context.orderFilterValidated, ICryptoFilterNone)
    }


}
