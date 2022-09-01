package common.models

import helpers.NONE
import kotlinx.datetime.Instant
import models.CryptoPair

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

    fun getCryptoPair(): CryptoPair {
        val raw = this.topic.substringAfter(":").split("_")

        return everyoneOrNull(raw.getOrNull(0), raw.getOrNull(1)) { (first, second) ->
            CryptoPair(first, second)
        } ?: CryptoPair.EMPTY
    }

    companion object {
        val EMPTY = ExmoInData()
    }

    //TODO вынести куда-то в более высокий уровень, так как такой же оператор есть в других мапперах
    inline fun <T, R> everyoneOrNull(vararg args: T?, block: (List<T>) -> R): R? {
        return if (args.all { it != null }) {
            block(args.filterNotNull())
        } else {
            null
        }
    }
}
