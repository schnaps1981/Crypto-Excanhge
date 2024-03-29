package validation.filter

import context.CryptoOrderContext
import crypto.app.inmemory.OrderRepositoryInMemory
import helpers.NONE
import helpers.nowMicros
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import models.CryptoSettings
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
    private val settings by lazy {
        CryptoSettings(
            repoTest = OrderRepositoryInMemory()
        )
    }

    private val processor = CryptoOrderProcessor(settings)

    @Test
    fun `validate filter by date`() = runTest {
        val context = CryptoOrderContext(
            command = CryptoOrderCommands.READ,
            state = CryptoState.NONE,
            workMode = CryptoWorkMode.TEST,
            orderFilter = CryptoFilterByDate(
                orderDate = timeMills
            )
        )

        processor.exec(context)

        println(context)

        assertEquals(0, context.errors.size)
        assertNotEquals(CryptoState.FAILED, context.state)

        assertEquals(timeMills, (context.orderFilterValidated as CryptoFilterByDate).orderDate)
    }

    @Test
    fun `validate filter by date empty`() = runTest {
        val context = CryptoOrderContext(
            command = CryptoOrderCommands.READ,
            state = CryptoState.NONE,
            workMode = CryptoWorkMode.TEST,
            orderFilter = CryptoFilterByDate(
                orderDate = Instant.NONE
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

    companion object {
        private val timeMills = Instant.nowMicros
    }

}
