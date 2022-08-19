package serialize

import com.crypto.api.exmo.v1.models.ExmoRequest


fun apiExmoRequestSerialize(request: ExmoRequest): String = jacksonMapper.writeValueAsString(request)

fun apiExmoRequestDeserialize(json: String): ExmoRequest = jacksonMapper.readValue(json, ExmoRequest::class.java)
