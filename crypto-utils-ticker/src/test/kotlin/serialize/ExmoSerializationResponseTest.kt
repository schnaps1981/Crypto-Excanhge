package serialize

import com.crypto.api.exmo.v1.models.DataTicker
import com.crypto.api.exmo.v1.models.Event
import com.crypto.api.exmo.v1.models.ExmoResponse
import org.junit.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ExmoSerializationResponseTest {

    @Test
    fun `serialize exmo api response test`() {
        val dataTicker = DataTicker(
            buyPrice = "11.11",
            sellPrice = "22.22",
            lastTrade = "33.33",
            high = "100",
            low = "200",
            avg = "300",
            vol = "400",
            volCurr = "500",
            updated = 100
        )

        val response = ExmoResponse(
            id = 1,
            ts = 1000,
            code = 1,
            message = "hello-world",
            sessionId = "session-id",
            topic = "topic",
            error = "error",
            `data` = dataTicker,
            event = Event.ERROR
        )

        val jsonString = apiExmoResponseSerialize(response)

        println(jsonString)

        assertContains(jsonString, "\"id\":1")
        assertContains(jsonString, "\"ts\":1000")
        assertContains(jsonString, "\"code\":1")
        assertContains(jsonString, "\"message\":\"hello-world\"")
        assertContains(jsonString, "\"session_id\":\"session-id\"")
        assertContains(jsonString, "\"topic\":\"topic\"")
        assertContains(jsonString, "\"error\":\"error\"")
        assertContains(jsonString, "\"buy_price\":\"11.11\"")
        assertContains(jsonString, "\"sell_price\":\"22.22\"")
        assertContains(jsonString, "\"last_trade\":\"33.33\"")
        assertContains(jsonString, "\"high\":\"100\"")
        assertContains(jsonString, "\"low\":\"200\"")
        assertContains(jsonString, "\"vol\":\"400\"")
        assertContains(jsonString, "\"vol_curr\":\"500\"")
        assertContains(jsonString, "\"updated\":100")
        assertContains(jsonString, "\"event\":\"error")
    }

    @Test
    fun `exmo api response deserialize` () {
        val json = "{\"id\":1,\"ts\":1000,\"code\":1,\"message\":\"hello-world\",\"session_id\":\"session-id\",\"topic\":\"topic\",\"error\":\"error\",\"data\":{\"buy_price\":\"11.11\",\"sell_price\":\"22.22\",\"last_trade\":\"33.33\",\"high\":\"100\",\"low\":\"200\",\"avg\":\"300\",\"vol\":\"400\",\"vol_curr\":\"500\",\"updated\":100},\"event\":\"error\"}"

        val obj = apiExmoResponseDeserialize(json)

        println(obj)

        assertEquals(1, obj.id)
        assertEquals(1000, obj.ts)
        assertEquals(1, obj.code)
        assertEquals("hello-world", obj.message)
        assertEquals("session-id", obj.sessionId)
        assertEquals("topic", obj.topic)
        assertEquals("error", obj.error)
        assertEquals("11.11", obj.data?.buyPrice)
        assertEquals("22.22", obj.data?.sellPrice)
        assertEquals("33.33", obj.data?.lastTrade)
        assertEquals("100", obj.data?.high)
        assertEquals("200", obj.data?.low)
        assertEquals("300", obj.data?.avg)
        assertEquals("400", obj.data?.vol)
        assertEquals("500", obj.data?.volCurr)
        assertEquals(100, obj.data?.updated)
        assertEquals(100, obj.data?.updated)
        assertEquals(Event.ERROR, obj.event)
    }
}
