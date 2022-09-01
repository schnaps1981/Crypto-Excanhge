package serialize

import com.crypto.api.exmo.v1.models.ExmoResponse

fun apiExmoResponseSerialize(response: ExmoResponse): String = jacksonMapper.writeValueAsString(response)

fun apiExmoResponseDeserialize(json: String): ExmoResponse = jacksonMapper.readValue(json, ExmoResponse::class.java)
