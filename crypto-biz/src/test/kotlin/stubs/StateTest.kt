package stubs

import context.CryptoOrderContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import models.CryptoSettings
import models.CryptoState
import models.CryptoWorkMode
import org.junit.Test
import processors.CryptoOrderProcessor
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class StateTest {

    private val processor = CryptoOrderProcessor(CryptoSettings())

    @Test
    fun `init state test`() = runTest {
        val ctx = CryptoOrderContext(
            workMode = CryptoWorkMode.STUB,
            state = CryptoState.NONE,
            stubCase = CryptoOrderStubs.SUCCESS
        )

        processor.exec(ctx)

        println(ctx.state)

        assertEquals(ctx.state, CryptoState.RUNNING)
    }
}
