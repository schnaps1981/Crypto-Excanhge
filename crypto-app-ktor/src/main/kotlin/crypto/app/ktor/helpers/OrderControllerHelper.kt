package crypto.app.ktor.helpers

import LogWrapper
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
import toLog
import toTransport

suspend inline fun <reified Q : IRequest, reified R : IResponse> ApplicationCall.orderControllerHelper(
    logger: LogWrapper,
    logId: String,
    command: CryptoOrderCommands? = null,
    crossinline block: suspend CryptoOrderContext.() -> Unit
) {
    val ctx = CryptoOrderContext()
    try {
        logger.doWithLogging {
            val request = receive<Q>()

            ctx.fromTransport(request)

            logger.info(
                msg = "$command request is got",
                data = ctx.toLog("${logId}-got")
            )

            ctx.block()

            logger.info(
                msg = "$command request is handled",
                data = ctx.toLog("${logId}-handled")
            )

            val response = ctx.toTransport()

            respond(response)
        }

    } catch (e: Throwable) {
        command?.also { ctx.command = it }

        ctx.state = CryptoState.FAILED
        ctx.errors.add(e.asCryptoError())

        logger.error(
            msg = "Fail to handle $command request",
            e = e,
            data = ctx.toLog("${logId}-error")
        )

        ctx.block()

        val response = ctx.toTransport()
        respond(response)
    }
}
