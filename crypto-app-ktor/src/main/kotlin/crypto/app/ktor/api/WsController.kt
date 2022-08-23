package crypto.app.ktor.api

import OrderService
import apiV1RequestDeserialize
import apiV1ResponseSerialize
import com.crypto.api.v1.models.IRequest
import context.CryptoOrderContext
import context.addError
import crypto.app.ktor.helpers.KtorUserSession
import fromTransport
import helpers.asCryptoError
import helpers.nowMicros
import io.ktor.websocket.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.flow.*
import kotlinx.datetime.Instant
import ru.otus.otuskotlin.marketplace.logging.LogWrapper
import toTransport


suspend fun WebSocketSession.wsOrderHandler(
    service: OrderService,
    sessions: MutableSet<KtorUserSession>,
    logger: LogWrapper
) {
    val userSession = KtorUserSession(this)
    sessions.add(userSession)

    run {
        // обработка запроса на инициализацию
        outgoing.send(Frame.Text("Connection established"))
    }

    incoming
        .receiveAsFlow()

        .mapNotNull { it as? Frame.Text }

        .map { frame ->
            val jsonStr = frame.readText()
            apiV1RequestDeserialize<IRequest>(jsonStr)
        }

        .flowOn(Dispatchers.IO)

        .map { request ->
            val ctx = CryptoOrderContext(
                timeStart = Instant.nowMicros
            ).apply { fromTransport(request) }
            service.exec(ctx)
            ctx
        }

        .flowOn(Dispatchers.Default)

        .map {
            outgoing.send(Frame.Text(apiV1ResponseSerialize(it.toTransport())))
        }

        .flowOn(Dispatchers.IO)
        // Обработка исключений с завершением flow

        .catch { e ->
            if (e is ClosedReceiveChannelException) {
                sessions.remove(userSession)
            } else {
                val ctx = CryptoOrderContext(
                    timeStart = Instant.nowMicros
                )
                ctx.addError(e.asCryptoError())
                outgoing.send(Frame.Text(apiV1ResponseSerialize(ctx.toTransport())))
                userSession.fwSession.close(CloseReason(CloseReason.Codes.INTERNAL_ERROR, ""))
                sessions.remove(userSession)
            }
        }

        .collect()
}
