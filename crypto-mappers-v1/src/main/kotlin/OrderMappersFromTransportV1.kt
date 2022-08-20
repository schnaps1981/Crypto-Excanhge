import com.crypto.api.v1.models.*
import context.CryptoOrderContext
import errors.UnknownFilter
import errors.UnknownRequestClass
import kotlinx.datetime.Instant
import models.CryptoPair
import models.commands.CryptoOrderCommands
import models.filter.*
import stubs.CryptoOrderStubs

fun CryptoOrderContext.fromTransport(request: IRequest) = when (request) {
    is OrderCreateRequest -> fromTransport(request)
    is OrderReadRequest -> fromTransport(request)
    is OrderDeleteRequest -> fromTransport(request)
    else -> throw UnknownRequestClass(request.javaClass, this.javaClass)
}

fun CryptoOrderContext.fromTransport(request: OrderCreateRequest) {
    requestId = request.requestId()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubOrder()

    command = CryptoOrderCommands.CREATE

    orderRequest.pair = everyoneOrNull(request.pair?.first, request.pair?.second) { (first, second) ->
        CryptoPair(first, second)
    } ?: CryptoPair()

    orderRequest.quantity = request.quantity.toBigDecimalOrElse { 0.0.toBigDecimal() }
    orderRequest.price = request.price.toBigDecimalOrElse { 0.0.toBigDecimal() }
    orderRequest.orderType = request.orderType.transportToCryptoOrderType()

    userIdRequest = request.userId.toCryptoUserId()
}

fun CryptoOrderContext.fromTransport(request: OrderReadRequest) {
    requestId = request.requestId()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubOrder()

    command = CryptoOrderCommands.READ

    userIdRequest = request.userId.toCryptoUserId()

    orderFilter = request.filter.transportToCryptoOrderFilter()
}

fun CryptoOrderContext.fromTransport(request: OrderDeleteRequest) {
    requestId = request.requestId()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubOrder()

    command = CryptoOrderCommands.DELETE

    orderRequest.orderId = request.orderId.toCryptoOrderId()
    userIdRequest = request.userId.toCryptoUserId()
}

private fun IFilter?.transportToCryptoOrderFilter(): ICryptoFilter = when (this) {
    is FilterByDate -> fromTransport(this)
    is FilterByCurrency -> fromTransport(this)
    is FilterByState -> fromTransport(this)
    is FilterByType -> fromTransport(this)
    null -> CryptoFilterNoFilter()
    else -> throw UnknownFilter(this)
}

private fun fromTransport(filter: FilterByDate) = CryptoFilterByDate(orderDate = Instant.parse(filter.date ?: ""))

private fun fromTransport(filter: FilterByCurrency) = CryptoFilterByCurrency(ticker = filter.currency ?: "")

private fun fromTransport(filter: FilterByState) =
    CryptoFilterByState(orderState = filter.state.transportToCryptoOrderState())

private fun fromTransport(filter: FilterByType) =
    CryptoFilterByType(orderType = filter.type.transportToCryptoOrderType())

private fun Debug?.transportToStubOrder(): CryptoOrderStubs = when (this?.stub) {
    RequestDebugStubs.SUCCESS -> CryptoOrderStubs.SUCCESS
    RequestDebugStubs.NOT_FOUND -> CryptoOrderStubs.NOT_FOUND
    RequestDebugStubs.BAD_ID -> CryptoOrderStubs.BAD_ID
    RequestDebugStubs.CANNOT_DELETE -> CryptoOrderStubs.CANNOT_DELETE
    RequestDebugStubs.BAD_FILTER -> CryptoOrderStubs.BAD_FILTER
    null -> CryptoOrderStubs.NONE
}
