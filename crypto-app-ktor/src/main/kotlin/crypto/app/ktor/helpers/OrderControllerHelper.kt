package crypto.app.ktor.helpers

import com.crypto.api.v1.models.IRequest
import com.crypto.api.v1.models.IResponse
import context.CryptoOrderContext
import fromTransport
import helpers.asCryptoError
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import models.CryptoState
import models.commands.CryptoOrderCommands
import toTransport

suspend inline fun <reified Q : IRequest, reified R : IResponse>
        ApplicationCall.orderControllerHelper(
    command: CryptoOrderCommands? = null,
    block: CryptoOrderContext.() -> Unit
) {
    val ctx = CryptoOrderContext()
    try {
        val request = receive<Q>()
        ctx.fromTransport(request)
        ctx.block()
        val response = ctx.toTransport()
        respond(response)
    } catch (e: Throwable) {
        command?.also { ctx.command = it }
        ctx.state = CryptoState.FAILED
        ctx.errors.add(e.asCryptoError())
        ctx.block()
        val response = ctx.toTransport()
        respond(response)
    }
}
