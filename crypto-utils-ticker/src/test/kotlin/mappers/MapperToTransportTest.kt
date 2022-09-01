package mappers

import common.ExmoContext
import common.models.ExmoId
import common.models.ExmoMethod
import common.models.ExmoOutData
import org.junit.Test
import kotlin.test.assertEquals

class MapperToTransportTest {
    @Test
    fun `map to transport`() {
        val exmoOutData = ExmoOutData(
            id = ExmoId(10),
            method = ExmoMethod.SUBSCRIBE,
            topics = listOf("topic1", "topic2")
        )

        val context = ExmoContext().apply {
            this.exmoOutData = exmoOutData
        }

        val response = context.toTransport()

        println(response)

        assertEquals(10, response.id)
        assertEquals("SUBSCRIBE", response.method?.name)
        assertEquals("topic1", response.topics?.firstOrNull())
    }
}
