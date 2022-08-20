package mappers

import com.crypto.api.exmo.v1.models.DataTicker
import com.crypto.api.exmo.v1.models.Event
import com.crypto.api.exmo.v1.models.ExmoResponse
import common.ExmoContext
import common.models.ExmoEvent
import common.models.ExmoId
import org.junit.Test
import kotlin.test.assertEquals

class MapperFromTransportTest {
    @Test
    fun `map from transport`() {
        val dataTicker = DataTicker(
            buyPrice = "3.1",
            sellPrice = "1.1",
            lastTrade = "2.2",
            high = "3.3",
            low = "4.4",
            avg = "5.5",
            vol = "6.6",
            volCurr = "7.7",
            updated = 2000000000
        )

        val response = ExmoResponse(
            id = 100,
            ts = 1000000000,
            code = 3,
            message = "messge",
            sessionId = "sessionId",
            topic = "topic",
            error = "error",
            `data` = dataTicker,
            event = Event.INFO
        )

        val context = ExmoContext()
        context.fromTransport(response)

        println(context.exmoInData)

        assertEquals(ExmoId("100"), context.exmoInData.id)
        assertEquals(ExmoEvent.INFO, context.exmoInData.event)
        assertEquals("topic", context.exmoInData.topic)
        assertEquals("error", context.exmoInData.error)
        assertEquals("sessionId", context.exmoInData.sessionId)
    }
}