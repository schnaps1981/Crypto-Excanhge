package stubs

import context.CryptoOrderContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import models.CryptoState
import org.junit.Test
import processors.CryptoOrderProcessor
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class StateTest {

    private val processor = CryptoOrderProcessor()

    @Test
    fun `init state test`() = runTest {
        val ctx = CryptoOrderContext()

        processor.exec(ctx)

        println(ctx.state)

        assertEquals(ctx.state, CryptoState.RUNNING)
    }
}
