import com.crypto.api.v1.models.*
import context.CryptoAccountContext
import errors.UnknownRequestClass
import models.CryptoCurrency
import models.commands.CryptoAccountCommands
import stubs.CryptoAccountStubs

fun CryptoAccountContext.fromTransport(request: IRequest) = when (request) {
    is AccCreateRequest -> fromTransport(request)
    is AccDeleteRequest -> fromTransport(request)
    else -> throw UnknownRequestClass(request.javaClass, this.javaClass)
}

fun CryptoAccountContext.fromTransport(request: AccCreateRequest) {
    command = CryptoAccountCommands.CREATE

    requestId = request.requestId()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubAccount()

    accountRequest.balances = request.currencies.fromTransport()
}

fun CryptoAccountContext.fromTransport(request: AccDeleteRequest) {
    command = CryptoAccountCommands.DELETE

    requestId = request.requestId()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubAccount()

    accountRequest.id = request.userId.toCryptoUserId()
}

private fun List<Currency>?.fromTransport(): MutableList<CryptoCurrency> {
    return this.toMutableListNotNullOrEmpty {
        everyoneOrNull(it.ticker, it.value) { (ticker, value) ->
            CryptoCurrency(ticker as String, value.toBigDecimalOrElse { 0.0.toBigDecimal() })
        }
    }
}

//TODO роазобраться зачем стабы вообще
private fun Debug?.transportToStubAccount(): CryptoAccountStubs = when (this?.stub) {
    RequestDebugStubs.SUCCESS -> CryptoAccountStubs.SUCCESS
    else -> CryptoAccountStubs.NONE
}
