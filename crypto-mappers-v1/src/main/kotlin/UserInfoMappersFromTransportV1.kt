import com.crypto.api.v1.models.IRequest
import com.crypto.api.v1.models.UserBalancesRequest
import context.CryptoUserInfoContext
import errors.UnknownRequestClass
import models.commands.CryptoUserInfoCommands

fun CryptoUserInfoContext.fromTransport(request: IRequest) = when (request) {
    is UserBalancesRequest -> fromTransport(request)
    else -> throw UnknownRequestClass(request.javaClass, this.javaClass)
}

fun CryptoUserInfoContext.fromTransport(request: UserBalancesRequest) {
    command = CryptoUserInfoCommands.READ_BALANCE

    requestId = request.requestId()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()

    userInfoRequest.userId = request.userId.toCryptoUserId()
}
