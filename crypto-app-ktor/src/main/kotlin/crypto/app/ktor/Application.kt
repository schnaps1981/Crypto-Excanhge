package crypto.app.ktor

import OrderService
import RepoOrderSql
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import crypto.app.ktor.api.createOrder
import crypto.app.ktor.api.deleteOrder
import crypto.app.ktor.api.readOrders
import crypto.app.ktor.api.wsOrderHandler
import crypto.app.ktor.helpers.KtorUserSession
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import org.slf4j.event.Level

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused")
fun Application.module() {

    install(ContentNegotiation) {
        jackson {
            disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

            enable(SerializationFeature.INDENT_OUTPUT)
            writerWithDefaultPrettyPrinter()
        }
    }

    install(WebSockets)

    install(CallLogging) {
        level = Level.INFO
    }

    install(IgnoreTrailingSlash)

    val orderRepository = RepoOrderSql()
    //val orderRepository = OrderRepositoryInMemory(ttl = Duration.ofMinutes(10))
    val orderService = OrderService(orderRepository)

    val sessions = mutableSetOf<KtorUserSession>()

    routing {
        route("/order") {
            post("/create") {
                call.createOrder(orderService)
            }

            post("/read") {
                call.readOrders(orderService)
            }

            post("/delete") {
                call.deleteOrder(orderService)
            }
        }

        webSocket("/ws/order") {
            wsOrderHandler(orderService, sessions)
        }
    }
}
