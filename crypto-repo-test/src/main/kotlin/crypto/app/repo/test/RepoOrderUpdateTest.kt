package crypto.app.repo.test

import kotlinx.coroutines.runBlocking
import models.*
import org.junit.Test
import repository.DbOrderRequest
import repository.IOrderRepository
import kotlin.test.assertEquals


abstract class RepoOrderUpdateTest {
    abstract val repo: IOrderRepository

    @Test
    fun updateSuccess() {
        val result = runBlocking { repo.updateOrder(DbOrderRequest(updateObj)) }
        assertEquals(true, result.isSuccess)
        assertEquals(updateObj, result.result)
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun updateNotFound() {
        val result = runBlocking { repo.updateOrder(DbOrderRequest(updateObjNotFound)) }
        assertEquals(false, result.isSuccess)
        assertEquals(null, result.result)
        assertEquals(listOf(CryptoError(field = "id", message = "Not Found")), result.errors)
    }

    companion object : BaseInitOrder("update") {
        override val initObjects: List<CryptoOrder> = listOf(
            createInitTestModel(
                CryptoUserId("test-owner-123-read"),
                CryptoOrderType.BUY,
                CryptoOrderState.ACTIVE,
                CryptoPair("BTC", "USD")
            )
        )
        private val updateId = initObjects.first().orderId
        private val updateIdNotFound = CryptoOrderId("repo-update-not-found")

        private val updateObj = CryptoOrder(
            orderId = updateId,
            orderState = CryptoOrderState.COMPLETED
        )

        private val updateObjNotFound = CryptoOrder(
            orderId = updateIdNotFound
        )
    }
}
