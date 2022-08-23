import com.crypto.api.logs.models.*
import context.CryptoOrderContext
import helpers.NONE
import helpers.nowMicros
import kotlinx.datetime.Instant
import models.*
import models.filter.*
import java.math.BigDecimal
import java.util.*

fun CryptoOrderContext.toLog(logId: String) = CommonLogModel(messageId = UUID.randomUUID().toString(),
    messageTime = Instant.nowMicros.toString(),
    logId = logId,
    source = "crypto",
    order = toOrderLog(),
    errors = errors.map { it.toLog() })

fun CryptoError.toLog() = ErrorLogModel(
    message = message.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    code = code.takeIf { it.isNotBlank() },
    level = level.name
)

fun CryptoOrderContext.toOrderLog(): OrderLogModel? {
    return OrderLogModel(requestId = requestId.takeIf { it != CryptoRequestId.NONE }?.asString(),
        requestOrder = orderRequest.takeIf { it != CryptoOrder.EMPTY }?.toLog(),
        requestFilter = orderFilter.takeIf { it != ICryptoFilter.NONE }?.toLog(),
        responseOrders = ordersResponse.takeIf { it.isNotEmpty() }?.filter { it != CryptoOrder.EMPTY }
            ?.map { it.toLog() })
}

private fun CryptoOrder.toLog() = OrderLog(
    orderId = orderId.takeIf { it != CryptoOrderId.NONE }?.asString(),
    ownerId = ownerId.takeIf { it != CryptoUserId.NONE }?.asString(),
    created = created.takeIf { it != Instant.NONE }?.toString(),
    orderState = orderState.takeIf { it != CryptoOrderState.NONE }?.name,
    amount = amount.takeIf { it != BigDecimal.ZERO }?.toString(),
    quantity = quantity.takeIf { it != BigDecimal.ZERO }?.toString(),
    price = price.takeIf { it != BigDecimal.ZERO }?.toString(),
    orderType =orderType.takeIf { it != CryptoOrderType.NONE }?.name,
    pair = pair.takeIf { it != CryptoPair.EMPTY }?.toString(),
    permissions = setOf()//TODO permissions.takeIf { it.isNotEmpty() }?.map { it.name }?.toSet(),
)

private fun ICryptoFilter.toLog(): OrderFilterLog {
    val filterValue = when (this) {
        is CryptoFilterByDate -> this.orderDate.toString()

        is CryptoFilterByType -> this.orderType.name

        is CryptoFilterByState -> this.orderState.name

        is CryptoFilterByCurrency -> this.ticker

        else -> null
    }

    val filterType = when (this) {
        is CryptoFilterByDate -> CryptoFilterByDate::class.java.simpleName

        is CryptoFilterByType -> CryptoFilterByType::class.java.simpleName

        is CryptoFilterByState -> CryptoFilterByState::class.java.simpleName

        is CryptoFilterByCurrency -> CryptoFilterByCurrency::class.java.simpleName

        else -> null
    }

    return OrderFilterLog(
        filterType = filterType,
        filterValue = filterValue,
        searchTypes = setOf() //TODO searchTypes.takeIf { it.isNotEmpty() }?.map { it.name }?.toSet(),
    )
}
