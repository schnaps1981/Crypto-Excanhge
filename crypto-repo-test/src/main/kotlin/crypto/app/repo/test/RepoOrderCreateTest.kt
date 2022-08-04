package crypto.app.repo.test

import kotlinx.coroutines.runBlocking
import models.*
import org.junit.Test
import repository.DbOrderRequest
import repository.IOrderRepository
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

abstract class RepoOrderCreateTest {
    abstract val repo: IOrderRepository

    @Test
    fun createSuccess() {
        val result = runBlocking { repo.createOrder(DbOrderRequest(order)) }
        val expected = order.copy(orderId = result.result?.orderId ?: CryptoOrderId.NONE)

        assertEquals(true, result.isSuccess)
        assertEquals(expected, result.result)
        assertNotEquals(CryptoOrderId.NONE, result.result?.orderId)
        assertEquals(emptyList(), result.errors)
    }

    companion object : BaseInitOrder("create") {
        override val initObjects: List<CryptoOrder> = emptyList()

        val order = createInitTestModel(
            CryptoUserId("test-owner-123-create"),
            CryptoOrderType.BUY,
            CryptoOrderState.ACTIVE,
            CryptoPair("BTC", "USD")
        )
    }
}