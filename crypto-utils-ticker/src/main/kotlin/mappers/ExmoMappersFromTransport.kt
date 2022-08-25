package mappers

import com.crypto.api.exmo.v1.models.DataTicker
import com.crypto.api.exmo.v1.models.Event
import com.crypto.api.exmo.v1.models.ExmoResponse
import common.ExmoContext
import common.models.ExmoEvent
import common.models.ExmoId
import common.models.ExmoTickerData
import helpers.NONE
import kotlinx.datetime.Instant
import java.math.BigDecimal


fun ExmoContext.fromTransport(response: ExmoResponse) {
    exmoInData.id = response.id?.let { ExmoId(it) } ?: ExmoId.NONE

    exmoInData.timestamp = response.ts?.let {
        Instant.fromEpochMilliseconds(it)
    } ?: Instant.NONE //TODO здесь будет не округленный до мкс инстант

    exmoInData.code = response.code ?: 0
    exmoInData.message = response.message ?: ""
    exmoInData.sessionId = response.sessionId ?: ""
    exmoInData.topic = response.topic ?: ""
    exmoInData.error = response.error ?: ""
    exmoInData.data = response.data.fromTransport()
    exmoInData.event = response.event.fromTransport()
}

private fun Event?.fromTransport() = when (this) {
    null -> ExmoEvent.NONE
    Event.INFO -> ExmoEvent.INFO
    Event.SUBSCRIBED -> ExmoEvent.SUBSCRIBED
    Event.ERROR -> ExmoEvent.ERROR
    Event.UNSUBSCRIBED -> ExmoEvent.UNSUBSCRIBED
    Event.UPDATE -> ExmoEvent.UPDATE
}

private fun DataTicker?.fromTransport(): ExmoTickerData {
    return this?.let { it ->
        ExmoTickerData(
            buyPrice = it.buyPrice.toBigDecimalOrElse { BigDecimal.ZERO },
            sellPrice = it.sellPrice.toBigDecimalOrElse { BigDecimal.ZERO },
            lastTrade = it.lastTrade.toBigDecimalOrElse { BigDecimal.ZERO },
            high = it.high.toBigDecimalOrElse { BigDecimal.ZERO },
            low = it.low.toBigDecimalOrElse { BigDecimal.ZERO },
            avg = it.avg.toBigDecimalOrElse { BigDecimal.ZERO },
            vol = it.vol.toBigDecimalOrElse { BigDecimal.ZERO },
            volCurr = it.volCurr.toBigDecimalOrElse { BigDecimal.ZERO },

            updated = it.updated?.let {
                Instant.fromEpochMilliseconds(it)
            } ?: Instant.NONE //TODO здесь будет не округленный до мкс инстант
        )
    } ?: ExmoTickerData.EMPTY
}

//TODO вынести расширение в модуль более верхнего уровня
fun String?.toBigDecimalOrElse(block: () -> BigDecimal): BigDecimal =
    this?.toBigDecimalOrNull() ?: block()