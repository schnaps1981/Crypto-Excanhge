package context

import models.*
import models.commands.CryptoUserInfoCommands

data class CryptoUserInfoContext(
    override var state: CryptoState = CryptoState.NONE,
    override val errors: MutableList<CryptoError> = mutableListOf(),

    override var workMode: CryptoWorkMode = CryptoWorkMode.PROD,
    override var stubCase: CryptoStubs = CryptoStubs.NONE,

    override var requestId: CryptoRequestId = CryptoRequestId.NONE,

    var command: CryptoUserInfoCommands = CryptoUserInfoCommands.NONE,

    var userInfoRequest: CryptoUserInfo = CryptoUserInfo(),
    var userInfoResponse: CryptoUserInfo = CryptoUserInfo()

) : CryptoBaseContext
