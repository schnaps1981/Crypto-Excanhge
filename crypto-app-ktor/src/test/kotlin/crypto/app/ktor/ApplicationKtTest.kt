package crypto.app.ktor;

import com.crypto.api.v1.models.*
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jsonMapper
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationKtTest {

    @Test
    fun `test order create endpoint`() = testApplication {
        val client = createClient {
            this@testApplication.install(ContentNegotiation) {
                jackson {
                    disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

                    enable(SerializationFeature.INDENT_OUTPUT)
                    writerWithDefaultPrettyPrinter()
                }
            }
        }

        val response = client.post("/order/create") {




            contentType(ContentType.Application.Json)
            setBody(
                OrderCreateRequest(
                    requestId = "requestId-123",
                    requestType = "OrderCreate",
                    debug = Debug(
                        mode = RequestDebugMode.STUB,
                        stub = RequestDebugStubs.SUCCESS
                    ),
                    quantity = "1.4",
                    price = "100.3",
                    orderType = OrderType.SELL,
                    pair = TickerPair(
                        first = "BTC",
                        second = "ETH"
                    ),
                    userId = "123"
                )
            )
        }

        println(response.bodyAsText())

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("", response.bodyAsText())

    }
}
