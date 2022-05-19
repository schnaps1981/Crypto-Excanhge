import com.crypto.api.v1.models.*
import context.CryptoTickerContext
import context.CryptoUserInfoContext
import models.*
import models.commands.CryptoTickerCommands
import models.commands.CryptoUserInfoCommands
import org.junit.Test
import stubs.CryptoTickerStubs
import stubs.CryptoUserInfoStubs
import kotlin.test.assertEquals

class OrderMappersTest {

    @Test
    fun `user info (balances) to transport`() {
        val context = CryptoUserInfoContext(
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
            command = CryptoUserInfoCommands.READ_BALANCE,
            userInfoResponse = CryptoUserInfo(
                userId = CryptoUserId("3423"),
                balances = listOf(
                    CryptoCurrency("BTC", 1.0.toBigDecimal()),
                    CryptoCurrency("USD", 100.0.toBigDecimal())
                )
            )
        )

        val response = context.toTransport() as UserBalancesResponse

        println(response)

        assertEquals(ResponseResult.SUCCESS, response.result)

        assertEquals("123", response.errors?.firstOrNull()?.code)
        assertEquals("non_fatal", response.errors?.firstOrNull()?.group)
        assertEquals("currencies", response.errors?.firstOrNull()?.field)
        assertEquals("couldn't create some balance", response.errors?.firstOrNull()?.message)

        assertEquals("BTC", response.currencies?.firstOrNull()?.ticker)
        assertEquals("1.0", response.currencies?.firstOrNull()?.value)

    }

    @Test
    fun `user info (balances) from transport`() {
        val request = UserBalancesRequest(
            requestId = "1234",
            debug = Debug(
                mode = RequestDebugMode.STUB,
                stub = RequestDebugStubs.SUCCESS
            ),
            userId = "user123"
        )

        val context = CryptoUserInfoContext()
        context.fromTransport(request)

        println(context)

        assertEquals(CryptoUserInfoStubs.SUCCESS, context.stubCase)
        assertEquals(CryptoWorkMode.STUB, context.workMode)

        assertEquals(CryptoUserInfoCommands.READ_BALANCE, context.command)

        assertEquals(CryptoUserId("user123"), context.userInfoRequest.userId)
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

        assertEquals(CryptoTickerStubs.SUCCESS, context.stubCase)
        assertEquals(CryptoWorkMode.STUB, context.workMode)

        assertEquals(CryptoRequestId("1234"), context.requestId)
        assertEquals(CryptoTickerCommands.READ_TICKER, context.command)

        assertEquals("BTC", context.ratesRequest.first)
        assertEquals("USD", context.ratesRequest.second)
    }
}
