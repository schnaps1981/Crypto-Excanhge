package context

import helpers.NONE
import kotlinx.datetime.Instant
import models.*
import models.commands.CryptoOrderCommands
import models.filter.ICryptoFilter
import repository.IOrderRepository
import stubs.CryptoOrderStubs

data class CryptoOrderContext(
    override var state: CryptoState = CryptoState.NONE,
    override val errors: MutableList<CryptoError> = mutableListOf(),

    override var workMode: CryptoWorkMode = CryptoWorkMode.PROD,
    override var stubCase: CryptoOrderStubs = CryptoOrderStubs.NONE,

    override var requestId: CryptoRequestId = CryptoRequestId.NONE,

    override var command: CryptoOrderCommands = CryptoOrderCommands.NONE,

    override var timeStart: Instant = Instant.NONE,

    var orderRequest: CryptoOrder = CryptoOrder(),

    var orderResponse: CryptoOrder = CryptoOrder(),
    var ordersResponse: MutableList<CryptoOrder> = mutableListOf(),

    var orderValidating: CryptoOrder = CryptoOrder(),
    var orderValidated: CryptoOrder = CryptoOrder(),

    var orderFilter: ICryptoFilter = ICryptoFilter.NONE,

    var orderFilterValidating: ICryptoFilter = ICryptoFilter.NONE,
    var orderFilterValidated: ICryptoFilter = ICryptoFilter.NONE,

    var userIdRequest: CryptoUserId = CryptoUserId.NONE,

    var orderRepo: IOrderRepository = IOrderRepository.NONE,

    var orderRepoPrepare: CryptoOrder = CryptoOrder()

) : CryptoBaseContext<CryptoOrderStubs, CryptoOrderCommands>
