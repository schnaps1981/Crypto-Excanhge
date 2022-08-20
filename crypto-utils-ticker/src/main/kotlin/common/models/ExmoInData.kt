package common.models

import helpers.NONE
import kotlinx.datetime.Instant

data class ExmoInData(
    var id: ExmoId = ExmoId.NONE,
    var timestamp: Instant = Instant.NONE,
    var event: ExmoEvent = ExmoEvent.NONE,
    var topic: String = "",
    var data: ExmoTickerData = ExmoTickerData.EMPTY,
    var code: Int = -1,
    var message: String = "",
    var sessionId: String = "",
    var error: String = ""

) {
    fun deepCopy() = ExmoInData(
        id = this@ExmoInData.id,
        timestamp = this@ExmoInData.timestamp,
        event = this@ExmoInData.event,
        topic = this@ExmoInData.topic,
        data = this@ExmoInData.data.deepCopy(),
        code = this@ExmoInData.code,
        message = this@ExmoInData.message,
        sessionId = this@ExmoInData.sessionId,
        error = this@ExmoInData.error
    )

    companion object {
        val EMPTY = ExmoInData()
    }
}
