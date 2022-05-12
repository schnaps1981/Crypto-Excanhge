package context

import models.*
import models.commands.CryptoAccountCommands

data class CryptoAccountContext(
    override var state: CryptoState = CryptoState.NONE,
    override val errors: MutableList<CryptoError> = mutableListOf(),

    override var workMode: CryptoWorkMode = CryptoWorkMode.PROD,
    override var stubCase: CryptoStubs = CryptoStubs.NONE,

    override var requestId: CryptoRequestId = CryptoRequestId.NONE,

    var command: CryptoAccountCommands = CryptoAccountCommands.NONE,

    var accountRequest: CryptoAccount = CryptoAccount(),
    var accountResponse: CryptoAccount = CryptoAccount()

) : CryptoBaseContext
