package mappers

import com.crypto.api.exmo.v1.models.ExmoRequest
import com.crypto.api.exmo.v1.models.Method
import common.ExmoContext
import common.models.ExmoId
import common.models.ExmoMethod

fun ExmoContext.toTransport() = ExmoRequest(
    id = exmoOutData.id.takeIf { it != ExmoId.NONE }?.asInt(),
    method = exmoOutData.method.toTransport(),
    topics = exmoOutData.topics.map {
        it
    }

)

private fun ExmoMethod.toTransport(): Method? = when (this) {
    ExmoMethod.SUBSCRIBE -> Method.SUBSCRIBE
    ExmoMethod.UNSUBSCRIBE -> Method.UNSUBSCRIBE
    ExmoMethod.NONE -> null
}
