package common.models

import helpers.NONE
import kotlinx.datetime.Instant
import java.math.BigDecimal

data class ExmoTickerData(
    var buyPrice: BigDecimal = BigDecimal.ZERO,
    var sellPrice: BigDecimal = BigDecimal.ZERO,
    var lastTrade: BigDecimal = BigDecimal.ZERO,
    var high: BigDecimal = BigDecimal.ZERO,
    var low: BigDecimal = BigDecimal.ZERO,
    var avg: BigDecimal = BigDecimal.ZERO,
    var vol: BigDecimal = BigDecimal.ZERO,
    var volCurr: BigDecimal = BigDecimal.ZERO,
    var updated: Instant = Instant.NONE
) {
    fun deepCopy() = ExmoTickerData(
        buyPrice = this@ExmoTickerData.buyPrice,
        sellPrice = this@ExmoTickerData.sellPrice,
        lastTrade = this@ExmoTickerData.lastTrade,
        high = this@ExmoTickerData.high,
        low = this@ExmoTickerData.low,
        avg = this@ExmoTickerData.avg,
        vol = this@ExmoTickerData.vol,
        volCurr = this@ExmoTickerData.volCurr,
        updated = this@ExmoTickerData.updated,
    )

    companion object {
        val EMPTY = ExmoTickerData()
    }
}
