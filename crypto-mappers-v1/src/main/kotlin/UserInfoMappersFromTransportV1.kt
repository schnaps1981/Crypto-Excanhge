import com.crypto.api.v1.models.Debug
import com.crypto.api.v1.models.IRequest
import com.crypto.api.v1.models.RequestDebugStubs
import com.crypto.api.v1.models.UserBalancesRequest
import context.CryptoUserInfoContext
import errors.UnknownRequestClass
import models.commands.CryptoUserInfoCommands
import stubs.CryptoUserInfoStubs

fun CryptoUserInfoContext.fromTransport(request: IRequest) = when (request) {
    is UserBalancesRequest -> fromTransport(request)
    else -> throw UnknownRequestClass(request.javaClass, this.javaClass)
}

fun CryptoUserInfoContext.fromTransport(request: UserBalancesRequest) {
    command = CryptoUserInfoCommands.READ_BALANCE

    requestId = request.requestId()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubUserInfo()

    userInfoRequest.userId = request.userId.toCryptoUserId()
}

//TODO роазобраться зачем стабы вообще
private fun Debug?.transportToStubUserInfo(): CryptoUserInfoStubs = when (this?.stub) {
    RequestDebugStubs.SUCCESS -> CryptoUserInfoStubs.SUCCESS
    else -> CryptoUserInfoStubs.NONE
}
