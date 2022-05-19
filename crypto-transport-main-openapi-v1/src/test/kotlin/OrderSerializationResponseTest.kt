import com.crypto.api.v1.models.*
import org.junit.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class OrderSerializationResponseTest {

    @Test
    fun `serialize order create response test`() {
        val request = OrderCreateResponse(
            orderId = "orderId"
        )

        val jsonString = apiV1ResponseSerialize(request)

        println(jsonString)

        assertContains(jsonString, "\"responseType\":\"OrderCreate\"")
        assertContains(jsonString, "\"orderId\":\"orderId\"")
    }

    @Test
    fun `deserialize order create response test`() {
        val response =
            "{\"responseType\":\"OrderCreate\",\"requestId\":null,\"result\":null,\"errors\":null,\"orderId\":\"orderId\"}"

        val decoded = apiV1ResponseDeserialize<OrderCreateResponse>(response)

        println(decoded)

        assertEquals("OrderCreate", decoded.responseType)
        assertEquals("orderId", decoded.orderId)

    }

    @Test
    fun `serialize order delete response test`() {
        val request = OrderDeleteResponse(result = ResponseResult.SUCCESS, deleteResult = DeleteResult.SUCCESS)

        val jsonString = apiV1ResponseSerialize(request)

        println(jsonString)

        assertContains(jsonString, "\"responseType\":\"OrderDelete\"")
        assertContains(jsonString, "\"result\":\"success\"")
        assertContains(jsonString, "\"deleteResult\":\"success\"")
    }

    @Test
    fun `deserialize order delete request test`() {
        val response =
            "{\"responseType\":\"OrderDelete\",\"requestId\":null,\"result\":\"success\",\"errors\":null, \"deleteResult\":\"success\"}"

        val decoded = apiV1ResponseDeserialize<OrderDeleteResponse>(response)

        println(decoded)

        assertEquals("OrderDelete", decoded.responseType)
        assertEquals(ResponseResult.SUCCESS, decoded.result)
        assertEquals(DeleteResult.SUCCESS, decoded.deleteResult)
    }

    @Test
    fun `serialize order read response test`() {
        val request = OrderReadResponse(
            orders = listOf(
                Order(
                    pair = TickerPair(first = "BTC", second = "USD"),
                    orderId = "orderId_123",
                    price = "1.0",
                    orderType = OrderType.SELL,
                    quantity = "10.0",
                    orderState = OrderState.COMPLETED,
                    created = 111,
                    amount = "100.0"
                )
            )
        )

        val jsonString = apiV1ResponseSerialize(request)

        println(jsonString)
        assertContains(jsonString, "\"responseType\":\"OrderRead\"")
        assertContains(jsonString, "\"first\":\"BTC\"")
        assertContains(jsonString, "\"second\":\"USD\"")
        assertContains(jsonString, "\"quantity\":\"10.0\"")
        assertContains(jsonString, "\"price\":\"1.0\"")
        assertContains(jsonString, "\"orderType\":\"sell\"")
        assertContains(jsonString, "\"orderId\":\"orderId_123\"")
        assertContains(jsonString, "\"created\":111")
        assertContains(jsonString, "\"orderState\":\"completed\"")
        assertContains(jsonString, "\"amount\":\"100.0\"")

    }

    @Test
    fun `deserialize order read response`() {
        val response =
            "{\"responseType\":\"OrderRead\",\"requestId\":null,\"result\":null,\"errors\":null,\"orders\":[{\"pair\":{\"first\":\"BTC\",\"second\":\"USD\"},\"quantity\":10.0,\"price\":1.0,\"orderType\":\"sell\",\"orderId\":\"orderId_123\",\"created\":111,\"orderState\":\"completed\",\"amount\":100.0}]}"

        val decoded = apiV1ResponseDeserialize<OrderReadResponse>(response)

        println(decoded)

        assertEquals("OrderRead", decoded.responseType)
        assertEquals("BTC", decoded.orders?.get(0)?.pair?.first)
        assertEquals("USD", decoded.orders?.get(0)?.pair?.second)
        assertEquals("10.0", decoded.orders?.get(0)?.quantity)
        assertEquals("1.0", decoded.orders?.get(0)?.price)
        assertEquals(OrderType.SELL, decoded.orders?.get(0)?.orderType)
        assertEquals("orderId_123", decoded.orders?.get(0)?.orderId)
        assertEquals(111, decoded.orders?.get(0)?.created)
        assertEquals(OrderState.COMPLETED, decoded.orders?.get(0)?.orderState)
        assertEquals("100.0", decoded.orders?.get(0)?.amount)
    }
}
