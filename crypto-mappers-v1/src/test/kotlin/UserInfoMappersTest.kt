import com.crypto.api.v1.models.*
import context.CryptoOrderContext
import context.CryptoTickerContext
import kotlinx.datetime.Clock
import models.*
import models.commands.CryptoOrderCommands
import models.commands.CryptoTickerCommands
import models.filter.CryptoFilterByDate
import models.filter.CryptoFilterByType
import org.junit.Test
import stubs.CryptoOrderStubs
import stubs.CryptoTickerStubs
import kotlin.test.assertEquals

class UserInfoMappersTest {

    @Test
    fun `order create to transport`() {
        val context = CryptoOrderContext(
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
            command = CryptoOrderCommands.CREATE,
            orderResponse = CryptoOrder(orderId = CryptoOrderId("order123"))
        )

        val response = context.toTransport() as OrderCreateResponse

        println(response)

        assertEquals(ResponseResult.SUCCESS, response.result)

        assertEquals("123", response.errors?.firstOrNull()?.code)
        assertEquals("non_fatal", response.errors?.firstOrNull()?.group)
        assertEquals("currencies", response.errors?.firstOrNull()?.field)
        assertEquals("couldn't create some balance", response.errors?.firstOrNull()?.message)

        assertEquals("order123", response.orderId)
    }

    @Test
    fun `order create from transport`() {
        val request = OrderCreateRequest(
            requestId = "1234",
            debug = Debug(
                mode = RequestDebugMode.STUB,
                stub = RequestDebugStubs.SUCCESS
            ),
            userId = "user123",
            pair = TickerPair("BTC", "USD"),
            quantity = "1.0",
            price = "2.0",
            orderType = OrderType.SELL
        )

        val context = CryptoOrderContext()
        context.fromTransport(request)

        println(context)

        assertEquals(CryptoOrderStubs.SUCCESS, context.stubCase)
        assertEquals(CryptoWorkMode.STUB, context.workMode)

        assertEquals(CryptoOrderCommands.CREATE, context.command)

        assertEquals(CryptoUserId("user123"), context.userIdRequest)
        assertEquals(1.0.toBigDecimal(), context.orderRequest.quantity)
        assertEquals(2.0.toBigDecimal(), context.orderRequest.price)
        assertEquals(CryptoOrderType.SELL, context.orderRequest.orderType)
        assertEquals("BTC", context.orderRequest.pair.first)
        assertEquals("USD", context.orderRequest.pair.second)
    }

    @Test
    fun `order delete to transport`() {
        val context = CryptoOrderContext(
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
            command = CryptoOrderCommands.DELETE,
            orderResponse = CryptoOrder(orderState = CryptoOrderState.NONE)
        )

        val response = context.toTransport() as OrderDeleteResponse

        println(response)

        assertEquals(ResponseResult.SUCCESS, response.result)

        assertEquals("123", response.errors?.firstOrNull()?.code)
        assertEquals("non_fatal", response.errors?.firstOrNull()?.group)
        assertEquals("currencies", response.errors?.firstOrNull()?.field)
        assertEquals("couldn't create some balance", response.errors?.firstOrNull()?.message)

        assertEquals(DeleteResult.SUCCESS, response.deleteResult)
    }

    @Test
    fun `order delete from transport`() {
        val request = OrderDeleteRequest(
            requestId = "1234",
            debug = Debug(
                mode = RequestDebugMode.STUB,
                stub = RequestDebugStubs.SUCCESS
            ),
            userId = "user123",
            orderId = "order123"
        )

        val context = CryptoOrderContext()
        context.fromTransport(request)

        println(context)

        assertEquals(CryptoOrderStubs.SUCCESS, context.stubCase)
        assertEquals(CryptoWorkMode.STUB, context.workMode)

        assertEquals(CryptoOrderCommands.DELETE, context.command)

        assertEquals(CryptoUserId("user123"), context.userIdRequest)
        assertEquals(CryptoOrderId("order123"), context.orderRequest.orderId)
    }

    @Test
    fun `order read to transport`() {
        val context = CryptoOrderContext(
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
            command = CryptoOrderCommands.READ,
            ordersResponse = mutableListOf(
                CryptoOrder(
                    orderId = CryptoOrderId("order123"),
                    created = timestamp,
                    orderState = CryptoOrderState.COMPLETED,
                    amount = 2.0.toBigDecimal(),
                    quantity = 3.0.toBigDecimal(),
                    price = 4.0.toBigDecimal(),
                    orderType = CryptoOrderType.SELL,
                    pair = CryptoPair(
                        first = "BTC",
                        second = "USD"
                    )
                )
            ),

            )

        val response = context.toTransport() as OrderReadResponse

        println(response)

        assertEquals(ResponseResult.SUCCESS, response.result)

        assertEquals("123", response.errors?.firstOrNull()?.code)
        assertEquals("non_fatal", response.errors?.firstOrNull()?.group)
        assertEquals("currencies", response.errors?.firstOrNull()?.field)
        assertEquals("couldn't create some balance", response.errors?.firstOrNull()?.message)

        assertEquals("order123", response.orders?.firstOrNull()?.orderId)
        assertEquals(timestamp.toString(), response.orders?.firstOrNull()?.created)
        assertEquals(OrderState.COMPLETED, response.orders?.firstOrNull()?.orderState)
        assertEquals("2.0", response.orders?.firstOrNull()?.amount)
        assertEquals("3.0", response.orders?.firstOrNull()?.quantity)
        assertEquals("4.0", response.orders?.firstOrNull()?.price)
        assertEquals(OrderType.SELL, response.orders?.firstOrNull()?.orderType)
        assertEquals("BTC", response.orders?.firstOrNull()?.pair?.first)
        assertEquals("USD", response.orders?.firstOrNull()?.pair?.second)
    }

    @Test
    fun `order read from transport FilterByType`() {
        val request = OrderReadRequest(
            requestId = "1234",
            debug = Debug(
                mode = RequestDebugMode.STUB,
                stub = RequestDebugStubs.SUCCESS
            ),
            userId = "user123",
            filter = FilterByType(type = OrderType.SELL)
        )

        val context = CryptoOrderContext()
        context.fromTransport(request)

        println(context)

        assertEquals(CryptoOrderStubs.SUCCESS, context.stubCase)
        assertEquals(CryptoWorkMode.STUB, context.workMode)

        assertEquals(CryptoOrderCommands.READ, context.command)

        assertEquals(CryptoUserId("user123"), context.userIdRequest)
        assertEquals(
            CryptoFilterByType(orderType = CryptoOrderType.SELL).orderType,
            (context.orderFilter as CryptoFilterByType).orderType
        )
    }

    @Test
    fun `order read from transport FilterByDate`() {
        val request = OrderReadRequest(
            requestId = "1234",
            debug = Debug(
                mode = RequestDebugMode.STUB,
                stub = RequestDebugStubs.SUCCESS
            ),
            userId = "user123",
            filter = FilterByDate(date = "11111")
        )

        val context = CryptoOrderContext()
        context.fromTransport(request)

        println(context)

        assertEquals(CryptoOrderStubs.SUCCESS, context.stubCase)
        assertEquals(CryptoWorkMode.STUB, context.workMode)

        assertEquals(CryptoOrderCommands.READ, context.command)

        assertEquals(CryptoUserId("user123"), context.userIdRequest)
        assertEquals(
            CryptoFilterByDate(orderDate = "11111").orderDate,
            (context.orderFilter as CryptoFilterByDate).orderDate
        )
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

    companion object {
        val timestamp = Clock.System.now()
    }
}
