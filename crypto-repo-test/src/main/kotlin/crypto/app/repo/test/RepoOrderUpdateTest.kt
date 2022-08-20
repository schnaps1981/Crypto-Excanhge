package crypto.app.repo.test

import helpers.nowMicros
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Instant
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

        println(result)

        assertEquals(true, result.isSuccess)
        assertEquals(updateObj.orderId, result.result?.orderId)
        assertEquals(updateObj.orderState, result.result?.orderState)
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun updateNotFound() {
        val result = runBlocking { repo.updateOrder(DbOrderRequest(updateObjNotFound)) }

        println(result)

        assertEquals(false, result.isSuccess)
        assertEquals(null, result.result)
        assertEquals(listOf(CryptoError(field = "id", message = "Not Found")), result.errors)
    }

    companion object : BaseInitOrder("update") {
        val created: Instant = Instant.nowMicros

        override val initObjects: List<CryptoOrder> = listOf(
            createInitTestModel(
                CryptoUserId("test-owner-123-read"),
                CryptoOrderType.BUY,
                CryptoOrderState.ACTIVE,
                CryptoPair("BTC", "USD"),
                orderDate = created
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
