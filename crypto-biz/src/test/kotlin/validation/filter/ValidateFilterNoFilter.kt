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
class ValidateFilterNoFilter {
    private val settings by lazy {
        CryptoSettings(
            repoTest = OrderRepositoryInMemory()
        )
    }

    private val processor = CryptoOrderProcessor(settings)

    @Test
    fun `validate filter no filter`() = runTest {
        val context = CryptoOrderContext(
            command = CryptoOrderCommands.READ,
            state = CryptoState.NONE,
            workMode = CryptoWorkMode.TEST,
            orderFilter = CryptoFilterNoFilter()
        )

        processor.exec(context)

        println(context.orderFilterValidated)

        assertEquals(0, context.errors.size)
        assertNotEquals(CryptoState.FAILED, context.state)

        assertEquals(context.orderFilterValidated as CryptoFilterNoFilter, CryptoFilterNoFilter() )
    }
}
