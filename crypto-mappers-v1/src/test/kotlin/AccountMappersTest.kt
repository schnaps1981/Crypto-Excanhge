import com.crypto.api.v1.models.*
import context.CryptoAccountContext
import models.*
import models.commands.CryptoAccountCommands
import org.junit.Test
import kotlin.test.assertEquals

class AccountMappersTest {
    @Test
    fun `account create to transport`() {
        val context = CryptoAccountContext(
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
            command = CryptoAccountCommands.CREATE,
            accountResponse = CryptoAccount(
                id = CryptoUserId("789"),
                balances = mutableListOf(
                    CryptoCurrency(
                        ticker = "BTC",
                        value = 0.001
                    )
                )
            )
        )

        val response = context.toTransport() as AccCreateResponse

        println(response)

        assertEquals(ResponseResult.SUCCESS, response.result)
        assertEquals("789", response.userId)
        assertEquals("BTC", response.currencies?.firstOrNull()?.ticker)
        assertEquals(0.001, response.currencies?.firstOrNull()?.value)

        assertEquals("123", response.errors?.firstOrNull()?.code)
        assertEquals("non_fatal", response.errors?.firstOrNull()?.group)
        assertEquals("currencies", response.errors?.firstOrNull()?.field)
        assertEquals("couldn't create some balance", response.errors?.firstOrNull()?.message)
    }

    @Test
    fun `account delete to transport`() {
        val context = CryptoAccountContext(
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
            command = CryptoAccountCommands.DELETE,
            accountResponse = CryptoAccount(
                id = CryptoUserId("789"),
                state = CryptoAccountState.INACTIVE,
                balances = mutableListOf(
                    CryptoCurrency(
                        ticker = "BTC",
                        value = 0.001
                    )
                )
            )
        )

        val response = context.toTransport() as AccDeleteResponse

        println(response)

        assertEquals(ResponseResult.SUCCESS, response.result)
        assertEquals(DeleteResult.SUCCESS, response.deleteResult)

        assertEquals("123", response.errors?.firstOrNull()?.code)
        assertEquals("non_fatal", response.errors?.firstOrNull()?.group)
        assertEquals("currencies", response.errors?.firstOrNull()?.field)
        assertEquals("couldn't create some balance", response.errors?.firstOrNull()?.message)
    }

    @Test
    fun `account create from transport`() {
        val request = AccCreateRequest(
            requestId = "1234",
            debug = Debug(
                mode = RequestDebugMode.STUB,
                stub = RequestDebugStubs.SUCCESS
            ),
            currencies = listOf(
                Currency("BTC", 0.01),
                Currency("USD", 100.0)
            )
        )

        val context = CryptoAccountContext()
        context.fromTransport(request)

        println(context)
        assertEquals(CryptoStubs.SUCCESS, context.stubCase)
        assertEquals(CryptoWorkMode.STUB, context.workMode)

        assertEquals(CryptoRequestId("1234"), context.requestId)
        assertEquals(CryptoAccountCommands.CREATE, context.command)
        assertEquals(CryptoCurrency("BTC", 0.01), context.accountRequest.balances.firstOrNull())
    }

    @Test
    fun `account delete from transport`() {
        val request = AccDeleteRequest(
            requestId = "1234",
            debug = Debug(
                mode = RequestDebugMode.STUB,
                stub = RequestDebugStubs.SUCCESS
            ),
            userId = "user123"
        )

        val context = CryptoAccountContext()
        context.fromTransport(request)

        println(context)

        assertEquals(CryptoStubs.SUCCESS, context.stubCase)
        assertEquals(CryptoWorkMode.STUB, context.workMode)

        assertEquals(CryptoRequestId("1234"), context.requestId)
        assertEquals(CryptoAccountCommands.DELETE, context.command)
        assertEquals(CryptoUserId("user123"), context.accountRequest.id)
    }
}
