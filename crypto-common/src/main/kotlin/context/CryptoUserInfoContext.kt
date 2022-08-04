package context

import helpers.NONE
import kotlinx.datetime.Instant
import models.*
import models.commands.CryptoUserInfoCommands
import stubs.CryptoUserInfoStubs

data class CryptoUserInfoContext(
    override var state: CryptoState = CryptoState.NONE,
    override val errors: MutableList<CryptoError> = mutableListOf(),

    override var workMode: CryptoWorkMode = CryptoWorkMode.PROD,
    override var stubCase: CryptoUserInfoStubs = CryptoUserInfoStubs.NONE,

    override var requestId: CryptoRequestId = CryptoRequestId.NONE,

    override var command: CryptoUserInfoCommands = CryptoUserInfoCommands.NONE,

    override var timeStart: Instant = Instant.NONE,

    var userInfoRequest: CryptoUserInfo = CryptoUserInfo(),
    var userInfoResponse: CryptoUserInfo = CryptoUserInfo()

) : CryptoBaseContext<CryptoUserInfoStubs, CryptoUserInfoCommands>
