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
            id = 10,
            method = Method.SUBSCRIBE,
            topics = listOf("BTC/USD", "ETH/RUB")
        )

        val jsonString = apiExmoRequestSerialize(request)

        println(jsonString)

        assertContains(jsonString, "\"id\":\"10\"")
        assertContains(jsonString, "\"method\":\"subscribe\"")
        assertContains(jsonString, "\"topics\":[\"BTC/USD\",\"ETH/RUB\"")
    }

    @Test
    fun `deserialize EXMO api request test`() {
        val jsonString = "{\"id\":\"10\",\"method\":\"subscribe\",\"topics\":[\"BTC/USD\",\"ETH/RUB\"]}"

        val obj = apiExmoRequestDeserialize(jsonString)

        println(obj)

        assertEquals(10, obj.id)
        assertEquals(Method.SUBSCRIBE, obj.method)
        assertEquals(2, obj.topics?.size)
        assertEquals("BTC/USD", obj.topics?.firstOrNull())
    }
}
