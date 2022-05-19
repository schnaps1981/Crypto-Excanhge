package context

import models.*
import models.commands.CryptoAccountCommands
import stubs.CryptoAccountStubs

data class CryptoAccountContext(
    override var state: CryptoState = CryptoState.NONE,
    override val errors: MutableList<CryptoError> = mutableListOf(),

    override var workMode: CryptoWorkMode = CryptoWorkMode.PROD,
    override var stubCase: CryptoAccountStubs = CryptoAccountStubs.NONE,

    override var requestId: CryptoRequestId = CryptoRequestId.NONE,

    override var command: CryptoAccountCommands = CryptoAccountCommands.NONE,

    var accountRequest: CryptoAccount = CryptoAccount(),
    var accountResponse: CryptoAccount = CryptoAccount()

) : CryptoBaseContext<CryptoAccountStubs, CryptoAccountCommands>
