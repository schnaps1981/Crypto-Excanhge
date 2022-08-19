package serialize

import com.crypto.api.exmo.v1.models.ExmoRequest
import com.crypto.api.exmo.v1.models.Method
import org.junit.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ExmoSerializationRequestTest {

    @Test
    fun `serialize EXMO api request test`() {
        val request = ExmoRequest(
            id = "exmo-id",
            method = Method.SUBSCRIBE,
            topics = listOf("BTC/USD", "ETH/RUB")
        )

        val jsonString = apiExmoRequestSerialize(request)

        println(jsonString)

        assertContains(jsonString, "\"id\":\"exmo-id\"")
        assertContains(jsonString, "\"method\":\"subscribe\"")
        assertContains(jsonString, "\"topics\":[\"BTC/USD\",\"ETH/RUB\"")
    }

    @Test
    fun `deserialize EXMO api request test`() {
        val jsonString = "{\"id\":\"exmo-id\",\"method\":\"subscribe\",\"topics\":[\"BTC/USD\",\"ETH/RUB\"]}"

        val obj = apiExmoRequestDeserialize(jsonString)

        println(obj)

        assertEquals("exmo-id", obj.id)
        assertEquals(Method.SUBSCRIBE, obj.method)
        assertEquals(2, obj.topics?.size)
        assertEquals("BTC/USD", obj.topics?.firstOrNull())
    }
}
