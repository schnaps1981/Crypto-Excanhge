package crypto.app.ktor

import OrderRepositorySql
import OrderService
import SQLDbConfig
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import crypto.app.inmemory.OrderRepositoryInMemory
import crypto.app.ktor.api.createOrder
import crypto.app.ktor.api.deleteOrder
import crypto.app.ktor.api.readOrders
import crypto.app.ktor.api.wsOrderHandler
import crypto.app.ktor.helpers.KtorUserSession
import crypto.app.ktor.helpers.fromEnvironment
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import logger
import models.CryptoSettings
import org.slf4j.event.Level
import java.time.Duration

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused")
fun Application.module(
    repoSettings: CryptoSettings? = null,
    dbConfig: SQLDbConfig = SQLDbConfig.fromEnvironment(environment)
) {

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

    val settings by lazy {
        repoSettings ?: CryptoSettings(
            repoTest = OrderRepositoryInMemory(ttl = Duration.ofMinutes(10)),
            repoProd = OrderRepositorySql(dbConfig)
        )
    }

    val orderService = OrderService(settings)

    val sessions = mutableSetOf<KtorUserSession>()

    val logger = logger("CryptoKtorLogger")

    routing {
        route("/order") {
            post("/create") {
                call.createOrder(orderService, logger)
            }

            post("/read") {
                call.readOrders(orderService, logger)
            }

            post("/delete") {
                call.deleteOrder(orderService, logger)
            }
        }

        webSocket("/ws/order") {
            wsOrderHandler(orderService, sessions, logger)
        }
    }
}
