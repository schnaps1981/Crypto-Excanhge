import com.crypto.api.v1.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class SerializationResponseTest {

    @Test
    fun serializeAccCreateResponseTest() {
        val accCreateResponse = AccCreateResponse(userId = "user123")

        val jsonString = apiV1ResponseSerialize(accCreateResponse)

        println(jsonString)
        assertContains(jsonString, "\"userId\":\"user123\"")

    }

    @Test
    fun deserializeAccCreateResponseTest() {
        val jsonString =
            "{\"responseType\":\"create\",\"requestId\":null,\"result\":null,\"errors\":null,\"userId\":\"user123\"}"

        val decoded = apiV1ResponseDeserialize<AccCreateResponse>(jsonString)

        println(decoded)

        assertEquals("user123", decoded.userId)
    }

    @Test
    fun serializeAccDeleteResponseTest() {
        val accDeleteResponse = AccDeleteResponse(result = ResponseResult.SUCCESS)

        val jsonString = apiV1ResponseSerialize((accDeleteResponse))

        println(jsonString)
        assertContains(jsonString, "\"result\":\"success\"")
    }

    @Test
    fun deserializeAccDeleteResponseTest() {
        val jsonString = "{\"responseType\":\"delete\",\"requestId\":null,\"result\":\"success\",\"errors\":null}"

        val decoded = apiV1ResponseDeserialize<AccDeleteResponse>(jsonString)
        println(decoded)

        assertEquals(ResponseResult.SUCCESS, decoded.result)
    }

    @Test
    fun deserializeIResponseTest() {
        val jsonString = "{\"responseType\":\"delete\",\"requestId\":null,\"result\":\"success\",\"errors\":null}"

        val decoded = apiV1ResponseDeserialize<IResponse>(jsonString) as AccDeleteResponse
        println(decoded)

        assertEquals(ResponseResult.SUCCESS, decoded.result)
    }
}
