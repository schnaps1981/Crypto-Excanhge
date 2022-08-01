package crypto.app.ktor.helpers

import io.ktor.websocket.*
import models.IClientSession

data class KtorUserSession(
    override val fwSession: WebSocketSession
) : IClientSession<WebSocketSession>
