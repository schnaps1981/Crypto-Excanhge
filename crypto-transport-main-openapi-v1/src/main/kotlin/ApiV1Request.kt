import com.crypto.api.v1.models.IRequest

fun apiV1RequestSerialize(request: IRequest): String = jacksonMapper.writeValueAsString(request)

@Suppress("UNCHECKED_CAST")
fun <T : IRequest> apiV1RequestDeserialize(json: String): T =
    jacksonMapper.readValue(json, IRequest::class.java) as T
