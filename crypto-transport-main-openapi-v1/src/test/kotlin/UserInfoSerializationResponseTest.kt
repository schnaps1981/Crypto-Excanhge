import com.crypto.api.v1.models.Currency
import com.crypto.api.v1.models.UserBalancesResponse
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class UserInfoSerializationResponseTest {

    @Test
    fun `serialize user balance response`() {
        val request = UserBalancesResponse(
            currencies = listOf(
                Currency("BTC", "1.0"),
                Currency("USD", "1000.0")
            )
        )

        val jsonString = apiV1ResponseSerialize(request)

        println(jsonString)
        assertContains(jsonString, "\"ticker\":\"BTC\"")
        assertContains(jsonString, "\"value\":\"1.0\"")
        assertContains(jsonString, "\"ticker\":\"USD\"")
        assertContains(jsonString, "\"value\":\"1000.0\"")
        assertContains(jsonString, "\"responseType\":\"UserBalancesRead\"")

    }

    @Test
    fun `deserialize user balance response`() {
        val jsonString =
            "{\"responseType\":\"UserBalancesRead\",\"requestId\":null,\"result\":null,\"errors\":null,\"currencies\":[{\"ticker\":\"BTC\",\"value\":1.0},{\"ticker\":\"USD\",\"value\":1000.0}]}"

        val decoded = apiV1ResponseDeserialize<UserBalancesResponse>(jsonString)

        println(decoded)

        val currencyPairsAsMap = decoded.currencies?.associate { it.ticker to it.value } ?: emptyMap()

        assertContains(currencyPairsAsMap, "BTC")
        assertContains(currencyPairsAsMap, "USD")

        assertEquals(currencyPairsAsMap["BTC"], "1.0")
        assertEquals(currencyPairsAsMap["USD"], "1000.0")

        assertEquals("UserBalancesRead", decoded.responseType)
    }
}
