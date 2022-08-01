package crypto.app.ktor

import apiV1RequestSerialize
import apiV1ResponseDeserialize
import com.crypto.api.v1.models.*
import io.ktor.client.plugins.websocket.*
import io.ktor.server.testing.*
import io.ktor.websocket.*
import kotlin.test.Test
import kotlin.test.assertEquals

class WebsocketTest {

    @Test
    fun `order create websocket`() {
        testApplication {
            val client = createClient {
                install(WebSockets)
            }

            client.webSocket("/ws/order") {
                run {
                    val incoming = incoming.receive()
                    val response = (incoming as Frame.Text).readText()
                    assertEquals("Connection established", response)
                }

                val request = OrderCreateRequest(
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

                send(Frame.Text(apiV1RequestSerialize(request)))
                val incoming = incoming.receive()
                val response = apiV1ResponseDeserialize<OrderCreateResponse>((incoming as Frame.Text).readText())

                println(response)
                assertEquals("requestId-123", response.requestId)
                assertEquals("orderID-123213", response.orderId)
            }
        }
    }

    @Test
    fun `order read websocket`() {
        testApplication {
            val client = createClient {
                install(WebSockets)
            }

            client.webSocket("/ws/order") {
                run {
                    val incoming = incoming.receive()
                    val response = (incoming as Frame.Text).readText()
                    assertEquals("Connection established", response)
                }

                val request = OrderReadRequest(
                    requestId = "requestId-123",
                    requestType = "OrderRead",
                    debug = Debug(
                        mode = RequestDebugMode.STUB,
                        stub = RequestDebugStubs.SUCCESS
                    ),
                    userId = "123"
                )

                send(Frame.Text(apiV1RequestSerialize(request)))
                val incoming = incoming.receive()
                val response = apiV1ResponseDeserialize<OrderReadResponse>((incoming as Frame.Text).readText())

                println(response)
                assertEquals("requestId-123", response.requestId)
                assertEquals(2, response.orders?.size)
            }
        }
    }

    @Test
    fun `order delete websocket`() {
        testApplication {
            val client = createClient {
                install(WebSockets)
            }

            client.webSocket("/ws/order") {
                run {
                    val incoming = incoming.receive()
                    val response = (incoming as Frame.Text).readText()
                    assertEquals("Connection established", response)
                }

                val request = OrderDeleteRequest(
                    requestId = "requestId-123",
                    requestType = "OrderDelete",
                    debug = Debug(
                        mode = RequestDebugMode.STUB,
                        stub = RequestDebugStubs.SUCCESS
                    ),
                    orderId = "123"
                )

                send(Frame.Text(apiV1RequestSerialize(request)))
                val incoming = incoming.receive()
                val response = apiV1ResponseDeserialize<OrderDeleteResponse>((incoming as Frame.Text).readText())

                println(response)
                assertEquals("requestId-123", response.requestId)
                assertEquals("success", response.deleteResult?.value)
            }
        }
    }
}
