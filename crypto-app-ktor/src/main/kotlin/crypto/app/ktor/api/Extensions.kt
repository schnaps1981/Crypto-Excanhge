package crypto.app.ktor.api

import com.crypto.api.v1.models.ResponseResult
import context.CryptoBaseContext
import models.CryptoError
import models.CryptoState

fun CryptoBaseContext<*, *>.successResponse(context: CryptoBaseContext<*, *>.() -> Unit) = apply(context)
    .apply {
        state = CryptoState.RUNNING
    }

fun CryptoBaseContext<*, *>.errorResponse(error: (CryptoError) -> CryptoError) =
    apply {
        state = CryptoState.FAILED
        errors.add(error(buildError()))
    }

private fun buildError() = CryptoError(
    field = "_", code = ResponseResult.ERROR.value
)
