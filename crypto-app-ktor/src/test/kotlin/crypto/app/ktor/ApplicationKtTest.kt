package crypto.app.ktor

import OrderStubs
import com.crypto.api.v1.models.*
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.testing.*
import org.junit.Test
import kotlin.test.assertEquals


class ApplicationKtTest {

    private fun ApplicationTestBuilder.httpClient() = createClient {
        install(ContentNegotiation) {
            jackson {
                disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

                enable(SerializationFeature.INDENT_OUTPUT)
                writerWithDefaultPrettyPrinter()
            }
        }
    }

    @Test
    fun `test order create endpoint`() = testApplication {
        val client = httpClient()

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

        val resp = response.body<OrderCreateResponse>()

        println(response.bodyAsText())

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(OrderStubs.oneOrderStub.orderId.asString(), resp.orderId)

    }

    @Test
    fun `test order read endpoint`() = testApplication {
        val client = httpClient()

        val response = client.post("/order/read") {

            contentType(ContentType.Application.Json)
            setBody(
                OrderReadRequest(
                    requestId = "requestId-123",
                    requestType = "OrderRead",
                    debug = Debug(
                        mode = RequestDebugMode.STUB,
                        stub = RequestDebugStubs.SUCCESS
                    ),
                    userId = "123"
                )
            )
        }

        val resp = response.body<OrderReadResponse>()

        println(response.bodyAsText())

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(true, resp.orders?.isNotEmpty())
    }

    @Test
    fun `test order delete endpoint`() = testApplication {
        val client = httpClient()

        val response = client.post("/order/delete") {

            contentType(ContentType.Application.Json)
            setBody(
                OrderDeleteRequest(
                    requestId = "requestId-123",
                    requestType = "OrderDelete",
                    debug = Debug(
                        mode = RequestDebugMode.STUB,
                        stub = RequestDebugStubs.SUCCESS
                    ),
                    orderId = "123"
                )
            )
        }

        val resp = response.body<OrderDeleteResponse>()

        println(response.bodyAsText())

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(DeleteResult.SUCCESS, resp.deleteResult)
    }
}
