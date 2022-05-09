import com.crypto.api.v1.models.UserBalancesRequest
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class UserInfoSerializationRequestTest {

    @Test
    fun `serialize user balance request`() {
        val request = UserBalancesRequest(userId = "User123")

        val jsonString = apiV1RequestSerialize(request)

        println(jsonString)
        assertContains(jsonString, "\"requestType\":\"UserBalancesRead\"")
        assertContains(jsonString, "\"userId\":\"User123\"")

    }

    @Test
    fun `deserialize user balance request`() {
        val jsonString =
            "{\"requestType\":\"UserBalancesRead\",\"requestId\":null,\"userId\":\"User123\"}"

        val decoded = apiV1RequestDeserialize<UserBalancesRequest>(jsonString)

        println(decoded)

        assertEquals("UserBalancesRead", decoded.requestType)
        assertEquals("User123", decoded.userId)
    }
}
