package crypto.app.repo.test

import kotlinx.coroutines.runBlocking
import models.*
import org.junit.Test
import repository.DbOrderIdRequest
import repository.IOrderRepository
import kotlin.test.assertEquals


abstract class RepoOrderReadTest {
    abstract val repo: IOrderRepository

    @Test
    fun readSuccess() {
        val result = runBlocking { repo.readOrder(DbOrderIdRequest(successId)) }

        assertEquals(true, result.isSuccess)
        assertEquals(readSuccessStub, result.result)
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun readNotFound() {
        val result = runBlocking { repo.readOrder(DbOrderIdRequest(notFoundId)) }

        assertEquals(false, result.isSuccess)
        assertEquals(null, result.result)
        assertEquals(
            listOf(CryptoError(field = "id", message = "Not Found")),
            result.errors
        )
    }

    companion object : BaseInitOrder("read") {
        override val initObjects: List<CryptoOrder> = listOf(
            createInitTestModel(
                CryptoUserId("test-owner-123-read"),
                CryptoOrderType.BUY,
                CryptoOrderState.ACTIVE,
                CryptoPair("BTC", "USD")
            )
        )
        private val readSuccessStub = initObjects.first()

        val successId = readSuccessStub.orderId
        val notFoundId = CryptoOrderId("repo-read-notFound")
    }
}
