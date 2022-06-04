import com.crypto.api.v1.models.*
import context.CryptoTickerContext
import errors.UnknownRequestClass
import models.CryptoPair
import models.commands.CryptoTickerCommands
import stubs.CryptoTickerStubs


fun CryptoTickerContext.fromTransport(request: IRequest) = when (request) {
    is TickerRequest -> fromTransport(request)
    is SupportedCurrenciesRequest -> fromTransport(request)
    else -> throw UnknownRequestClass(request.javaClass, this.javaClass)
}

fun CryptoTickerContext.fromTransport(request: TickerRequest) {
    command = CryptoTickerCommands.READ_TICKER

    requestId = request.requestId()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubTicker()

    ratesRequest = everyoneOrNull(request.first, request.second) { (first, second) ->
        CryptoPair(first, second)
    } ?: CryptoPair()
}

fun CryptoTickerContext.fromTransport(request: SupportedCurrenciesRequest) {
    command = CryptoTickerCommands.READ_CURRENCIES

    requestId = request.requestId()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubTicker()
}

private fun Debug?.transportToStubTicker(): CryptoTickerStubs = when (this?.stub) {
    RequestDebugStubs.SUCCESS -> CryptoTickerStubs.SUCCESS
    else -> CryptoTickerStubs.NONE
}
