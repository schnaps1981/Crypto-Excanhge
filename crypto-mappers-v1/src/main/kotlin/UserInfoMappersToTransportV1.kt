import com.crypto.api.v1.models.Currency
import com.crypto.api.v1.models.IResponse
import com.crypto.api.v1.models.ResponseResult
import com.crypto.api.v1.models.UserBalancesResponse
import context.CryptoUserInfoContext
import errors.UnknownCommand
import models.CryptoState
import models.commands.CryptoUserInfoCommands

fun CryptoUserInfoContext.toTransport(): IResponse = when (val cmd = command) {
    CryptoUserInfoCommands.READ_BALANCE -> toTransportReadUserBalance()
    CryptoUserInfoCommands.NONE -> throw UnknownCommand(cmd.name, this.javaClass)
}

fun CryptoUserInfoContext.toTransportReadUserBalance() = UserBalancesResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == CryptoState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    currencies = userInfoResponse.balances.map { Currency(it.ticker, it.value) }
)
