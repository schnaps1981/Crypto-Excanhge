import com.crypto.api.v1.models.ResponseResult
import context.CryptoBaseContext
import models.CryptoError
import models.CryptoState

inline fun <reified T> CryptoBaseContext<*, *>.successResponse(context: T.() -> Unit): T =
    (this as T).apply(context)
        .apply {
            state = CryptoState.RUNNING
        }

inline fun <reified T> CryptoBaseContext<*, *>.errorResponse(error: (CryptoError) -> CryptoError): T =
    apply {
        state = CryptoState.FAILED
        errors.add(error(buildError()))
    } as T

fun buildError() = CryptoError(
    field = "_", code = ResponseResult.ERROR.value
)
