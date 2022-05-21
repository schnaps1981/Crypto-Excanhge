package context

import models.*
import models.commands.CryptoOrderCommands
import models.filter.ICryptoFilter
import stubs.CryptoOrderStubs

data class CryptoOrderContext(
    override var state: CryptoState = CryptoState.NONE,
    override val errors: MutableList<CryptoError> = mutableListOf(),

    override var workMode: CryptoWorkMode = CryptoWorkMode.PROD,
    override var stubCase: CryptoOrderStubs = CryptoOrderStubs.NONE,

    override var requestId: CryptoRequestId = CryptoRequestId.NONE,

    override var command: CryptoOrderCommands = CryptoOrderCommands.NONE,

    var orderRequest: CryptoOrder = CryptoOrder(),
    var orderResponse: CryptoOrder = CryptoOrder(),
    var ordersResponse: MutableList<CryptoOrder> = mutableListOf(),

    var orderFilter: ICryptoFilter = ICryptoFilter.NONE,

    var userIdRequest: CryptoUserId = CryptoUserId.NONE

) : CryptoBaseContext<CryptoOrderStubs, CryptoOrderCommands>
