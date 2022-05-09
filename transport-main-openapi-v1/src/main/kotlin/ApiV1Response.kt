import com.crypto.api.v1.models.IResponse

fun apiV1ResponseSerialize(response: IResponse): String = jacksonMapper.writeValueAsString(response)

@Suppress("UNCHECKED_CAST")
fun <T : IResponse> apiV1ResponseDeserialize(json: String): T =
    jacksonMapper.readValue(json, IResponse::class.java) as T
