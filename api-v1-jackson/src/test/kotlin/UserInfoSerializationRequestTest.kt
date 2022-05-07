import com.crypto.api.v1.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class UserInfoSerializationRequestTest {

    @Test
    fun serializeUserBalancesRequestTest() {
        val request = UserBalancesRequest(userId = "User123")

        val jsonString = apiV1RequestSerialize(request)

        println(jsonString)
        assertContains(jsonString, "\"requestType\":\"read\"")
        assertContains(jsonString, "\"userId\":\"User123\"")

    }

    @Test
    fun deserializeUserBalancesRequestTest() {
        val jsonString =
            "{\"requestType\":\"read\",\"requestId\":null,\"userId\":\"User123\"}"

        val decoded = apiV1RequestDeserialize<UserBalancesRequest>(jsonString)

        println(decoded)

        assertEquals("read", decoded.requestType)
        assertEquals("User123", decoded.userId)
    }
}
