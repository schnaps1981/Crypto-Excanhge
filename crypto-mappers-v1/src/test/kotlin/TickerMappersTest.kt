import com.crypto.api.v1.models.*
import context.CryptoTickerContext
import models.*
import models.commands.CryptoTickerCommands
import org.junit.Test
import kotlin.test.assertEquals

class TickerMappersTest {
    @Test
    fun `supported currencies to transport`() {
        val context = CryptoTickerContext(
            state = CryptoState.RUNNING,
            errors = mutableListOf(
                CryptoError(
                    code = "123",
                    group = "non_fatal",
                    field = "currencies",
                    message = "couldn't create some balance"
                )
            ),
            requestId = CryptoRequestId("456"),
            command = CryptoTickerCommands.READ_CURRENCIES,
            supportedCurrenciesResponse = mutableListOf("BTC", "USD", "ETH")
        )

        val response = context.toTransport() as SupportedCurrenciesResponse

        println(response)

        assertEquals(ResponseResult.SUCCESS, response.result)

        assertEquals("123", response.errors?.firstOrNull()?.code)
        assertEquals("non_fatal", response.errors?.firstOrNull()?.group)
        assertEquals("currencies", response.errors?.firstOrNull()?.field)
        assertEquals("couldn't create some balance", response.errors?.firstOrNull()?.message)

        assertEquals(response.currencies?.firstOrNull(), "BTC")
    }

    @Test
    fun `read ticker to transport`() {
        val context = CryptoTickerContext(
            state = CryptoState.RUNNING,
            errors = mutableListOf(
                CryptoError(
                    code = "123",
                    group = "non_fatal",
                    field = "currencies",
                    message = "couldn't create some balance"
                )
            ),
            requestId = CryptoRequestId("456"),
            command = CryptoTickerCommands.READ_TICKER,
            ratesResponse = CryptoPairRate(
                pair = CryptoPair("BTC", "USD"),
                rate = 10.0.toBigDecimal()
            )
        )

        val response = context.toTransport() as TickerResponse

        println(response)

        assertEquals(ResponseResult.SUCCESS, response.result)

        assertEquals("123", response.errors?.firstOrNull()?.code)
        assertEquals("non_fatal", response.errors?.firstOrNull()?.group)
        assertEquals("currencies", response.errors?.firstOrNull()?.field)
        assertEquals("couldn't create some balance", response.errors?.firstOrNull()?.message)

        assertEquals("10.0", response.rate)
        assertEquals("BTC", response.pair?.first)
        assertEquals("USD", response.pair?.second)
    }

    @Test
    fun `supported currencies from transport`() {
        val request = SupportedCurrenciesRequest(
            requestId = "1234",
            debug = Debug(
                mode = RequestDebugMode.STUB,
                stub = RequestDebugStubs.SUCCESS
            )
        )

        val context = CryptoTickerContext()
        context.fromTransport(request)

        println(context)
        assertEquals(CryptoStubs.SUCCESS, context.stubCase)
        assertEquals(CryptoWorkMode.STUB, context.workMode)

        assertEquals(CryptoTickerCommands.READ_CURRENCIES, context.command)
    }

    @Test
    fun `ticker read from transport`() {
        val request = TickerRequest(
            requestId = "1234",
            debug = Debug(
                mode = RequestDebugMode.STUB,
                stub = RequestDebugStubs.SUCCESS
            ),
            first = "BTC",
            second = "USD"
        )

        val context = CryptoTickerContext()
        context.fromTransport(request)

        println(context)

        assertEquals(CryptoStubs.SUCCESS, context.stubCase)
        assertEquals(CryptoWorkMode.STUB, context.workMode)

        assertEquals(CryptoRequestId("1234"), context.requestId)
        assertEquals(CryptoTickerCommands.READ_TICKER, context.command)

        assertEquals("BTC", context.ratesRequest.first)
        assertEquals("USD", context.ratesRequest.second)

    }
}
