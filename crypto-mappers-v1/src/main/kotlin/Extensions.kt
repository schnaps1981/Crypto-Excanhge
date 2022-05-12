import com.crypto.api.v1.models.*
import models.*

fun IRequest?.requestId() = this?.requestId?.let { CryptoRequestId(it) } ?: CryptoRequestId.NONE
fun String?.toCryptoUserId() = this?.let { CryptoUserId(it) } ?: CryptoUserId.NONE
fun String?.toCryptoOrderId() = this?.let { CryptoOrderId(it) } ?: CryptoOrderId.NONE

fun Debug?.transportToWorkMode(): CryptoWorkMode = when (this?.mode) {
    RequestDebugMode.PROD -> CryptoWorkMode.PROD
    RequestDebugMode.TEST -> CryptoWorkMode.TEST
    RequestDebugMode.STUB -> CryptoWorkMode.STUB
    null -> CryptoWorkMode.PROD
}

fun Debug?.transportToStubCase(): CryptoStubs = when (this?.stub) {
    RequestDebugStubs.SUCCESS -> CryptoStubs.SUCCESS
    RequestDebugStubs.NOT_FOUND -> CryptoStubs.NOT_FOUND
    RequestDebugStubs.BAD_ID -> CryptoStubs.BAD_ID
    RequestDebugStubs.CANNOT_DELETE -> CryptoStubs.CANNOT_DELETE
    RequestDebugStubs.BAD_FILTER -> CryptoStubs.BAD_FILTER
    null -> CryptoStubs.NONE
}

fun OrderType?.transportToCryptoOrderType(): CryptoOrderType = when (this) {
    OrderType.SELL -> CryptoOrderType.SELL
    OrderType.BUY -> CryptoOrderType.BUY
    null -> CryptoOrderType.NONE
}

fun OrderState?.transportToCryptoOrderState(): CryptoOrderState = when (this) {
    OrderState.COMPLETED -> CryptoOrderState.COMPLETED
    OrderState.ACTIVE -> CryptoOrderState.ACTIVE
    OrderState.CANCELLED -> CryptoOrderState.CANCELLED
    null -> CryptoOrderState.NONE
}

fun List<CryptoError>.toTransportErrors(): List<Error>? = this
    .map { it.toTransportError() }
    .toList()
    .takeIf { it.isNotEmpty() }

fun CryptoError.toTransportError() = Error(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() },
)

fun CryptoPair.toTransport(): TickerPair = TickerPair(this.first, this.second)

inline fun <T, R> everyoneOrNull(vararg args: T?, block: (List<T>) -> R): R? {
    return if (args.all { it != null }) {
        block(args.filterNotNull())
    } else {
        null
    }
}

inline fun <T, R> List<T>?.toMutableListNotNullOrEmpty(block: (T) -> R?): MutableList<R> {
    return this?.mapNotNull { block(it) }?.toMutableList() ?: mutableListOf()
}
