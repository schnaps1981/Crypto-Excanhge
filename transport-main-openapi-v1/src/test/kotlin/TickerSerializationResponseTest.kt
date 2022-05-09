import com.crypto.api.v1.models.SupportedCurrenciesResponse
import com.crypto.api.v1.models.TickerPair
import com.crypto.api.v1.models.TickerResponse
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertIs

class TickerSerializationResponseTest {

    @Test
    fun `serialize supported currencies ticker response`() {
        val response = SupportedCurrenciesResponse(currencies = listOf("BTC", "USD", "EUR", "RUB"))

        val jsonString = apiV1ResponseSerialize(response)

        println(jsonString)
        assertContains(jsonString, "\"responseType\":\"CurrenciesRead\"")
    }

    @Test
    fun `deserialize supported currencies ticker response`() {
        val jsonString =
            "{\"responseType\":\"CurrenciesRead\",\"requestId\":null,\"result\":null,\"errors\":null,\"currencies\":[\"BTC\",\"USD\",\"EUR\",\"RUB\"]}"

        val decoded = apiV1ResponseDeserialize<SupportedCurrenciesResponse>(jsonString)

        println(decoded)

        assertEquals("CurrenciesRead", decoded.responseType)
        assertIs<Boolean>(decoded.currencies?.let { it == listOf("BTC", "USD", "EUR", "RUB") })
    }

    @Test
    fun `serialize ticker response`() {
        val response = TickerResponse(
            pair = TickerPair("BTC", "USD"),
            rate = 1000.0
        )

        val jsonString = apiV1ResponseSerialize(response)

        println(jsonString)
    }

    @Test
    fun `deserialize ticker response`() {
        val jsonString =
            "{\"responseType\":\"TickerRead\",\"requestId\":null,\"result\":null,\"errors\":null,\"pair\":{\"first\":\"BTC\",\"second\":\"USD\"},\"rate\":1000.0}"

        val decoded = apiV1ResponseDeserialize<TickerResponse>(jsonString)

        println(decoded)

        assertEquals("TickerRead", decoded.responseType)
        assertEquals("BTC", decoded.pair?.first)
        assertEquals("USD", decoded.pair?.second)
        assertEquals(1000.0, decoded.rate)
    }
}
