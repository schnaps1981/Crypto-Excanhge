import com.crypto.api.v1.models.AccCreateRequest
import com.crypto.api.v1.models.AccDeleteRequest
import com.crypto.api.v1.models.CurrencyPair
import com.crypto.api.v1.models.IRequest
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class AccountSerializationRequestTest {

    @Test
    fun `serialize account create request`() {
        val accCreateRequest = AccCreateRequest(
            currencies = listOf(
                CurrencyPair("BTC", 1.0),
                CurrencyPair("USD", 1000.0)
            )
        )

        val jsonString = apiV1RequestSerialize(accCreateRequest)

        println(jsonString)
        assertContains(jsonString, "\"ticker\":\"BTC\"")
        assertContains(jsonString, "\"value\":1.0")
        assertContains(jsonString, "\"requestType\":\"AccountCreate\"")

    }

    @Test
    fun `deserialize account create request`() {
        val jsonString =
            "{\"requestType\":\"AccountCreate\",\"requestId\":null,\"currencies\":[{\"ticker\":\"BTC\",\"value\":1.0},{\"ticker\":\"USD\",\"value\":1000.0}]}"

        val decoded = apiV1RequestDeserialize<AccCreateRequest>(jsonString)

        println(decoded)

        val currencyPairsAsMap = decoded.currencies?.associate { it.ticker to it.value } ?: emptyMap()

        assertContains(currencyPairsAsMap, "BTC")
        assertContains(currencyPairsAsMap, "USD")

        assertEquals(currencyPairsAsMap["BTC"], 1.0)
        assertEquals(currencyPairsAsMap["USD"], 1000.0)
    }

    @Test
    fun `serialize account delete request`() {
        val accDeleteRequest = AccDeleteRequest(userId = "user123")

        val jsonString = apiV1RequestSerialize(accDeleteRequest)

        println(jsonString)
        assertContains(jsonString, "\"userId\":\"user123\"")
    }

    @Test
    fun `deserialize account delete request`() {
        val jsonString = "{\"requestType\":\"AccountDelete\",\"requestId\":null,\"userId\":\"user123\"}"

        val decoded = apiV1RequestDeserialize<AccDeleteRequest>(jsonString)
        println(decoded)

        assertEquals("user123", decoded.userId)
    }

    @Test
    fun `deserialize IRequest`() {
        val jsonString = "{\"requestType\":\"AccountDelete\",\"requestId\":null,\"userId\":\"user123\"}"

        val decoded = apiV1RequestDeserialize<IRequest>(jsonString) as AccDeleteRequest
        println(decoded)

        assertEquals("user123", decoded.userId)
    }
}
