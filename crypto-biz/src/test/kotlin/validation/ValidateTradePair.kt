package validation

import context.CryptoOrderContext
import crypto.app.inmemory.OrderRepositoryInMemory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import models.*
import models.commands.CryptoOrderCommands
import org.junit.Test
import processors.CryptoOrderProcessor
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@ExperimentalCoroutinesApi
class ValidateTradePair {
    private val settings by lazy {
        CryptoSettings(
            repoTest = OrderRepositoryInMemory()
        )
    }

    private val context = CryptoOrderContext(
        command = CryptoOrderCommands.CREATE,
        state = CryptoState.NONE,
        workMode = CryptoWorkMode.TEST,
        orderRequest = CryptoOrder(
            quantity = 1.0.toBigDecimal(),
            price = 1.0.toBigDecimal(),
            orderType = CryptoOrderType.BUY,
            amount = 1.0.toBigDecimal()
        )
    )

    private fun CryptoOrderContext.setTradePair(tradePair: CryptoPair) =
        this.copy(orderRequest = this.orderRequest.copy(pair = tradePair))

    private val processor = CryptoOrderProcessor(settings)

    private fun `validate trade pair incorrect field`(pair: CryptoPair) = runTest {
        val ctx = context.setTradePair(pair)

        processor.exec(ctx)

        println(ctx)

        assertEquals(1, ctx.errors.size)
        assertEquals(CryptoState.FAILED, ctx.state)

        val error = ctx.errors.firstOrNull()
        assertEquals("validation", error?.group)
        assertEquals("pair", error?.field)
        assertEquals("validation-pair-empty", error?.code)
    }

    @Test
    fun `validate trade pair correct`() = runTest {
        val pair = CryptoPair(
            first = "BTC",
            second = "USD"
        )

        val ctx = context.setTradePair(pair)

        processor.exec(ctx)

        assertEquals(0, ctx.errors.size)
        assertNotEquals(CryptoState.FAILED, ctx.state)
        assertEquals(pair, ctx.orderValidated.pair)
    }

    @Test
    fun `validate trade pair incorrect first`() = `validate trade pair incorrect field`(CryptoPair(second = "USD"))

    @Test
    fun `validate trade pair incorrect second`() = `validate trade pair incorrect field`(CryptoPair(first = "USD"))

    @Test
    fun `validate trade pair incorrect both`() = `validate trade pair incorrect field`(CryptoPair())
}
