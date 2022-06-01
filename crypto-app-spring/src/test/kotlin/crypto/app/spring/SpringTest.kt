package crypto.app.spring

import OrderStubs
import com.crypto.api.v1.models.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class SpringTest {
    @LocalServerPort
    val port: Int = 0

    @Autowired
    private lateinit var webClient: WebTestClient

    @Test
    fun `test order create endpoint`() {
        val requestBody = OrderCreateRequest(
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

        webClient
            .post()
            .uri("http://localhost:$port/order/create/")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(requestBody)
            .exchange()
            .expectStatus().isOk
            .expectBody(OrderCreateResponse::class.java).consumeWith {
                val resp = it.responseBody

                println(resp)

                assertThat(resp?.orderId).isEqualTo(OrderStubs.oneOrderStub.orderId.asString())
            }
    }

    @Test
    fun `test order read endpoint`() {
        val requestBody = OrderReadRequest(
            requestId = "requestId-123",
            requestType = "OrderRead",
            debug = Debug(
                mode = RequestDebugMode.STUB,
                stub = RequestDebugStubs.SUCCESS
            ),
            userId = "123"
        )

        webClient
            .post()
            .uri("http://localhost:$port/order/read/")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(requestBody)
            .exchange()
            .expectStatus().isOk
            .expectBody(OrderReadResponse::class.java).consumeWith {
                val resp = it.responseBody

                println(resp)

                assertThat(resp?.orders?.size).isEqualTo(2)
                assertThat(resp?.requestId).isEqualTo("requestId-123")
            }
    }

    @Test
    fun `test order delete endpoint`() {
        val requestBody = OrderDeleteRequest(
            requestId = "requestId-123",
            requestType = "OrderDelete",
            debug = Debug(
                mode = RequestDebugMode.STUB,
                stub = RequestDebugStubs.SUCCESS
            ),
            orderId = "123"
        )

        webClient
            .post()
            .uri("http://localhost:$port/order/delete/")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(requestBody)
            .exchange()
            .expectStatus().isOk
            .expectBody(OrderDeleteResponse::class.java).consumeWith {
                val resp = it.responseBody

                println(resp)

                assertThat(resp?.deleteResult).isEqualTo(DeleteResult.SUCCESS)
                assertThat(resp?.requestId).isEqualTo("requestId-123")
            }
    }
}
