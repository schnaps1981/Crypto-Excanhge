import com.crypto.api.v1.models.SupportedCurrenciesRequest
import com.crypto.api.v1.models.TickerRequest
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class TickerSerializationRequestTest {

    @Test
    fun `serialize supported currencies ticker request`() {
        val request = SupportedCurrenciesRequest()

        val jsonString = apiV1RequestSerialize(request)

        println(jsonString)
        assertContains(jsonString, "\"requestType\":\"CurrenciesRead\"")
    }

    @Test
    fun `deserialize supported currencies ticker request`() {
        val jsonString =
            "{\"requestType\":\"CurrenciesRead\",\"requestId\":null}"

        val decoded = apiV1RequestDeserialize<SupportedCurrenciesRequest>(jsonString)

        println(decoded)

        assertEquals("CurrenciesRead", decoded.requestType)

    }

    @Test
    fun `serialize ticker request`() {
        val request = TickerRequest(
            first = "BTC",
            second = "USD"
        )

        val jsonString = apiV1RequestSerialize(request)

        println(jsonString)

        assertContains(jsonString, "\"requestType\":\"TickerRead\"")
        assertContains(jsonString, "\"first\":\"BTC\"")
        assertContains(jsonString, "\"second\":\"USD\"")
    }

    @Test
    fun `deserialize ticker request`() {
        val jsonString = "{\"requestType\":\"TickerRead\",\"requestId\":null,\"first\":\"BTC\",\"second\":\"USD\"}"

        val decoded = apiV1RequestDeserialize<TickerRequest>(jsonString)

        println(decoded)

        assertEquals("TickerRead", decoded.requestType)
        assertEquals("BTC", decoded.first)
        assertEquals("USD", decoded.second)
    }

}
