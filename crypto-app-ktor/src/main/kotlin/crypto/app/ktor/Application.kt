package crypto.app.ktor

import OrderRepositorySql
import OrderService
import SQLDbConfig
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import crypto.app.inmemory.OrderRepositoryInMemory
import crypto.app.ktor.KtorAuthConfig.Companion.GROUPS_CLAIM
import crypto.app.ktor.api.createOrder
import crypto.app.ktor.api.deleteOrder
import crypto.app.ktor.api.readOrders
import crypto.app.ktor.api.wsOrderHandler
import crypto.app.ktor.helpers.KtorUserSession
import crypto.app.ktor.helpers.fromEnvironment
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import models.CryptoSettings
import org.slf4j.event.Level
import java.time.Duration

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused")
fun Application.module(
    repoSettings: CryptoSettings? = null,
    dbConfig: SQLDbConfig = SQLDbConfig.fromEnvironment(environment),
    authConfig: KtorAuthConfig = KtorAuthConfig(environment)
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

    install(Authentication) {
        jwt("auth-jwt") {
            realm = authConfig.realm
            verifier(
                JWT
                    .require(Algorithm.HMAC256(authConfig.secret))
                    .withAudience(authConfig.audience)
                    .withIssuer(authConfig.issuer)
                    .build()
            )
            validate { jwtCredential: JWTCredential ->
                when {
                    jwtCredential.payload.getClaim(GROUPS_CLAIM).asList(String::class.java).isNullOrEmpty() -> {
                        this@module.log.error("Groups claim must not be empty in JWT token")
                        null
                    }
                    else -> JWTPrincipal(jwtCredential.payload)
                }
            }
        }
    }

    routing {
        authenticate("auth-jwt") {
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
        }

        webSocket("/ws/order") {
            wsOrderHandler(orderService, sessions)
        }
    }
}
