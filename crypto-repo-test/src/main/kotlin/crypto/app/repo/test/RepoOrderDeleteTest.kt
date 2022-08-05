package crypto.app.repo.test

import kotlinx.coroutines.runBlocking
import models.*
import org.junit.Test
import repository.DbOrderIdRequest
import repository.IOrderRepository
import kotlin.test.assertEquals


abstract class RepoOrderDeleteTest {
    abstract val repo: IOrderRepository

    @Test
    fun deleteSuccess() {
        val result = runBlocking { repo.deleteOrder(DbOrderIdRequest(successId)) }

        println(result)

        assertEquals(true, result.isSuccess)
        assertEquals(null, result.result)
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun deleteNotFound() {
        val result = runBlocking { repo.deleteOrder(DbOrderIdRequest(notFoundId)) }

        println(result)

        assertEquals(false, result.isSuccess)
        assertEquals(null, result.result)
        assertEquals(
            listOf(CryptoError(field = "id", message = "Not Found")),
            result.errors
        )
    }

    companion object : BaseInitOrder("delete") {
        override val initObjects: List<CryptoOrder> = listOf(
            createInitTestModel(
                CryptoUserId("test-owner-123-delete"),
                CryptoOrderType.BUY,
                CryptoOrderState.ACTIVE,
                CryptoPair("BTC", "USD"),
                orderId = "delete-success"
            )
        )
        private val deleteSuccessStub = initObjects.first()
        val successId = deleteSuccessStub.orderId

        val notFoundId = CryptoOrderId("repo-delete-notFound")
    }
}
