import com.crypto.api.v1.models.*
import org.junit.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class OrderSerializationRequestTest {

    @Test
    fun `serialize order create request test`() {
        val request = OrderCreateRequest(
            pair = TickerPair(first = "BTC", second = "USD"),
            orderType = OrderType.BUY,
            price = 100.0,
            quantity = 1.0
        )

        val jsonString = apiV1RequestSerialize(request)

        println(jsonString)

        assertContains(jsonString, "\"requestType\":\"OrderCreate\"")
        assertContains(jsonString, "\"pair\":{\"first\":\"BTC\",\"second\":\"USD\"}")
        assertContains(jsonString, "\"quantity\":1.0")
        assertContains(jsonString, "\"price\":100.0")
        assertContains(jsonString, "\"orderType\":\"buy\"")
    }

    @Test
    fun `deserialize order create request test`() {
        val response =
            "{\"requestType\":\"OrderCreate\",\"requestId\":null,\"pair\":{\"first\":\"BTC\",\"second\":\"USD\"},\"quantity\":1.0,\"price\":100.0,\"orderType\":\"buy\"}"

        val decoded = apiV1RequestDeserialize<OrderCreateRequest>(response)

        println(decoded)

        assertEquals("OrderCreate", decoded.requestType)
        assertEquals("BTC", decoded.pair?.first)
        assertEquals("USD", decoded.pair?.second)
        assertEquals(1.0, decoded.quantity)
        assertEquals(100.0, decoded.price)
        assertEquals(OrderType.BUY, decoded.orderType)
    }

    @Test
    fun `serialize order delete request test`() {
        val request = OrderDeleteRequest(orderId = "idOrder")

        val jsonString = apiV1RequestSerialize(request)

        println(jsonString)

        assertContains(jsonString, "\"requestType\":\"OrderDelete\"")
        assertContains(jsonString, "\"orderId\":\"idOrder\"")
    }

    @Test
    fun `deserialize order delete request test`() {
        val response = "{\"requestType\":\"OrderDelete\",\"requestId\":null,\"orderId\":\"idOrder\"}"

        val decoded = apiV1RequestDeserialize<OrderDeleteRequest>(response)

        println(decoded)

        assertEquals("OrderDelete", decoded.requestType)
        assertEquals("idOrder", decoded.orderId)
    }

    @Test
    fun `serialize order read request with no filter`() {
        val request = OrderReadRequest()

        val jsonString = apiV1RequestSerialize(request)

        println(jsonString)
        assertContains(jsonString, "\"requestType\":\"OrderRead\"")
        assertContains(jsonString, "\"filter\":null")
    }

    @Test
    fun `deserialize order read request with no filter`() {
        val response = "{\"requestType\":\"OrderRead\",\"requestId\":null,\"filter\":null}"

        val decoded = apiV1RequestDeserialize<OrderReadRequest>(response)

        println(decoded)

        assertEquals("OrderRead", decoded.requestType)
        assertEquals(null, decoded.filter)
    }

    @Test
    fun `serialize order read request with filter by date`() {
        val request = OrderReadRequest(
            filter = FilterByDate(date = "11.11.11")
        )

        val jsonString = apiV1RequestSerialize(request)

        println(jsonString)
        assertContains(jsonString, "\"requestType\":\"OrderRead\"")
        assertContains(jsonString, "\"filterType\":\"byDate\"")
        assertContains(jsonString, "\"date\":\"11.11.11\"")
    }

    @Test
    fun `deserialize order read request with filter by date`() {
        val response =
            "{\"requestType\":\"OrderRead\",\"requestId\":null,\"filter\":{\"filterType\":\"byDate\",\"date\":\"11.11.11\"}}"

        val decoded = apiV1RequestDeserialize<OrderReadRequest>(response)

        println(decoded)

        assertEquals("OrderRead", decoded.requestType)
        assert(decoded.filter is FilterByDate)
        assertEquals("byDate", decoded.filter?.filterType)
        assertEquals("11.11.11", (decoded.filter as? FilterByDate)?.date)
    }

    @Test
    fun `serialize order read request with filter by state`() {
        val request = OrderReadRequest(
            filter = FilterByState(state = OrderState.ACTIVE)
        )

        val jsonString = apiV1RequestSerialize(request)

        println(jsonString)
        assertContains(jsonString, "\"requestType\":\"OrderRead\"")
        assertContains(jsonString, "\"filterType\":\"byState\"")
        assertContains(jsonString, "\"state\":\"active\"")
    }

    @Test
    fun `deserialize order read request with filter by state`() {
        val response =
            "{\"requestType\":\"OrderRead\",\"requestId\":null,\"filter\":{\"filterType\":\"byState\",\"state\":\"active\"}}"

        val decoded = apiV1RequestDeserialize<OrderReadRequest>(response)

        println(decoded)

        assertEquals("OrderRead", decoded.requestType)
        assert(decoded.filter is FilterByState)
        assertEquals("byState", decoded.filter?.filterType)
        assertEquals(OrderState.ACTIVE, (decoded.filter as? FilterByState)?.state)
    }

    @Test
    fun `serialize order read request with filter by type`() {
        val request = OrderReadRequest(
            filter = FilterByType(type = OrderType.BUY)
        )

        val jsonString = apiV1RequestSerialize(request)

        println(jsonString)
        assertContains(jsonString, "\"requestType\":\"OrderRead\"")
        assertContains(jsonString, "\"filterType\":\"byType\"")
        assertContains(jsonString, "\"type\":\"buy\"")
    }

    @Test
    fun `deserialize order read request with filter by type`() {
        val response =
            "{\"requestType\":\"OrderRead\",\"requestId\":null,\"filter\":{\"filterType\":\"byType\",\"type\":\"buy\"}}"

        val decoded = apiV1RequestDeserialize<OrderReadRequest>(response)

        println(decoded)

        assertEquals("OrderRead", decoded.requestType)
        assert(decoded.filter is FilterByType)
        assertEquals("byType", decoded.filter?.filterType)
        assertEquals(OrderType.BUY, (decoded.filter as? FilterByType)?.type)
    }

    @Test
    fun `serialize order read request with filter by currency`() {
        val request = OrderReadRequest(
            filter = FilterByCurrency(currency = "BTC")
        )

        val jsonString = apiV1RequestSerialize(request)

        println(jsonString)
        assertContains(jsonString, "\"requestType\":\"OrderRead\"")
        assertContains(jsonString, "\"filterType\":\"byCurrency\"")
        assertContains(jsonString, "\"currency\":\"BTC\"")
    }

    @Test
    fun `deserialize order read request with filter by currency`() {
        val response =
            "{\"requestType\":\"OrderRead\",\"requestId\":null,\"filter\":{\"filterType\":\"byCurrency\",\"currency\":\"BTC\"}}"

        val decoded = apiV1RequestDeserialize<OrderReadRequest>(response)

        println(decoded)

        assertEquals("OrderRead", decoded.requestType)
        assert(decoded.filter is FilterByCurrency)
        assertEquals("byCurrency", decoded.filter?.filterType)
        assertEquals("BTC", (decoded.filter as? FilterByCurrency?)?.currency)
    }
}
