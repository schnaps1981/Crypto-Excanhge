import com.crypto.api.v1.models.*
import context.CryptoOrderContext
import errors.UnknownCommand
import models.CryptoOrderState
import models.CryptoOrderType
import models.commands.CryptoOrderCommands

fun CryptoOrderContext.toTransport(): IResponse = when (val cmd = command) {
    CryptoOrderCommands.CREATE -> toTransportCreateOrder()
    CryptoOrderCommands.READ -> toTransportReadOrder()
    CryptoOrderCommands.DELETE -> toTransportDeleteOrder()
    CryptoOrderCommands.NONE -> throw UnknownCommand(cmd.name, this.javaClass)
}

fun CryptoOrderContext.toTransportCreateOrder() = OrderCreateResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = state.toTransportResponseResult(),
    errors = errors.toTransportErrors(),
    orderId = this.orderResponse.orderId.asString().takeIf { it.isNotBlank() }
)

fun CryptoOrderContext.toTransportReadOrder() = OrderReadResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = state.toTransportResponseResult(),
    errors = errors.toTransportErrors(),
    orders = ordersResponse.map { order ->
        Order(
            pair = order.pair.toTransport(),
            quantity = order.quantity.toString(),
            price = order.price.toString(),
            orderType = order.orderType.toTransport(),
            orderId = order.orderId.asString().takeIf { it.isNotBlank() },
            created = order.created.toString(),
            orderState = order.orderState.toTransport(),
            amount = order.amount.toString(),
            ownerId = order.ownerId.asString()
        )
    }
)

fun CryptoOrderContext.toTransportDeleteOrder() = OrderDeleteResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = state.toTransportResponseResult(),
    errors = errors.toTransportErrors(),
    deleteResult = if (orderResponse.orderState == CryptoOrderState.NONE) DeleteResult.SUCCESS else DeleteResult.ERROR
)

private fun CryptoOrderState.toTransport(): OrderState? = when (this) {
    CryptoOrderState.CANCELLED -> OrderState.CANCELLED
    CryptoOrderState.ACTIVE -> OrderState.ACTIVE
    CryptoOrderState.COMPLETED -> OrderState.COMPLETED
    CryptoOrderState.NONE -> null
}

private fun CryptoOrderType.toTransport(): OrderType? = when (this) {
    CryptoOrderType.BUY -> OrderType.BUY
    CryptoOrderType.SELL -> OrderType.SELL
    CryptoOrderType.NONE -> null
}
