import com.crypto.api.v1.models.AccCreateRequest
import com.crypto.api.v1.models.CurrencyPair
import kotlin.test.Test
import kotlin.test.assertContains

class SerializationRequestTest {
    @Test
    fun serializeAccCreateRequestTest() {
        val accCreateRequest = AccCreateRequest(
            currencies = listOf(
                CurrencyPair("BTC", 1.0),
                CurrencyPair("USD", 1000.0)
            )
        )

        val jsonString = moshiMapper.adapter(AccCreateRequest::class.java).serializeNulls().toJson(accCreateRequest)


        println(jsonString)
        assertContains(jsonString, "\"ticker\":\"BTC\"")
        assertContains(jsonString, "\"value\":1.0")
        assertContains(jsonString, "\"requestType\":\"create\"")
    }
}