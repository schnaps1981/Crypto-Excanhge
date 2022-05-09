import com.crypto.api.v1.models.AccCreateResponse
import com.crypto.api.v1.models.AccDeleteResponse
import com.crypto.api.v1.models.IResponse
import com.crypto.api.v1.models.ResponseResult
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class AccountSerializationResponseTest {

    @Test
    fun `serialize account create response`() {
        val accCreateResponse = AccCreateResponse(userId = "user123")

        val jsonString = apiV1ResponseSerialize(accCreateResponse)

        println(jsonString)
        assertContains(jsonString, "\"userId\":\"user123\"")

    }

    @Test
    fun `deserialize account create response`() {
        val jsonString =
            "{\"responseType\":\"AccountCreate\",\"requestId\":null,\"result\":null,\"errors\":null,\"userId\":\"user123\"}"

        val decoded = apiV1ResponseDeserialize<AccCreateResponse>(jsonString)

        println(decoded)

        assertEquals("user123", decoded.userId)
    }

    @Test
    fun `serialize account delete response`() {
        val accDeleteResponse = AccDeleteResponse(result = ResponseResult.SUCCESS)

        val jsonString = apiV1ResponseSerialize((accDeleteResponse))

        println(jsonString)
        assertContains(jsonString, "\"result\":\"success\"")
    }

    @Test
    fun `deserialize account delete response`() {
        val jsonString =
            "{\"responseType\":\"AccountDelete\",\"requestId\":null,\"result\":\"success\",\"errors\":null}"

        val decoded = apiV1ResponseDeserialize<AccDeleteResponse>(jsonString)
        println(decoded)

        assertEquals(ResponseResult.SUCCESS, decoded.result)
    }

    @Test
    fun `deserialize IResponse`() {
        val jsonString =
            "{\"responseType\":\"AccountDelete\",\"requestId\":null,\"result\":\"success\",\"errors\":null}"

        val decoded = apiV1ResponseDeserialize<IResponse>(jsonString) as AccDeleteResponse
        println(decoded)

        assertEquals(ResponseResult.SUCCESS, decoded.result)
    }
}
