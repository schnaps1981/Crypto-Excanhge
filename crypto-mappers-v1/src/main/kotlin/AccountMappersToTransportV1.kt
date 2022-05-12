import com.crypto.api.v1.models.*
import context.CryptoAccountContext
import errors.UnknownCommand
import models.CryptoAccountState
import models.CryptoCurrency
import models.CryptoState
import models.commands.CryptoAccountCommands

fun CryptoAccountContext.toTransport(): IResponse = when (val cmd = command) {
    CryptoAccountCommands.CREATE -> toTransportAccountCreate()
    CryptoAccountCommands.DELETE -> toTransportAccountDelete()
    CryptoAccountCommands.NONE -> throw UnknownCommand(cmd.name, this.javaClass)
}

fun CryptoAccountContext.toTransportAccountCreate() = AccCreateResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == CryptoState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    userId = accountResponse.id.asString().takeIf { it.isNotBlank() },
    currencies = accountResponse.balances.toTransportCurrency()
)

fun CryptoAccountContext.toTransportAccountDelete() = AccDeleteResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == CryptoState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    deleteResult = if (accountResponse.state == CryptoAccountState.INACTIVE) DeleteResult.SUCCESS else DeleteResult.ERROR
)

private fun MutableList<CryptoCurrency>.toTransportCurrency(): List<Currency> =
    this.map { Currency(it.ticker, it.value) }
