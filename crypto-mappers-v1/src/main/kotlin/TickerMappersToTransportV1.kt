import com.crypto.api.v1.models.*
import context.CryptoTickerContext
import errors.UnknownCommand
import models.CryptoState
import models.commands.CryptoTickerCommands

fun CryptoTickerContext.toTransport(): IResponse = when (val cmd = command) {
    CryptoTickerCommands.READ_TICKER -> toTransportReadTicker()
    CryptoTickerCommands.READ_CURRENCIES -> toTransportReadCurrencies()
    CryptoTickerCommands.NONE -> throw UnknownCommand(cmd.name, this.javaClass)
}

fun CryptoTickerContext.toTransportReadTicker() = TickerResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == CryptoState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    pair = TickerPair(ratesResponse.pair.first, ratesResponse.pair.second),
    rate = ratesResponse.rate
)

fun CryptoTickerContext.toTransportReadCurrencies() = SupportedCurrenciesResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == CryptoState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    currencies = supportedCurrenciesResponse
)
