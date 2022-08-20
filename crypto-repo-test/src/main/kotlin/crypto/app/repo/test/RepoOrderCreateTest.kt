package crypto.app.repo.test

import helpers.nowMicros
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Instant
import models.*
import org.junit.Test
import repository.DbOrderRequest
import repository.IOrderRepository
import kotlin.test.assertEquals

abstract class RepoOrderCreateTest {
    abstract val repo: IOrderRepository

    @Test
    fun createSuccess() {
        val result = runBlocking { repo.createOrder(DbOrderRequest(order)) }
        val expected = order.copy(orderId = result.result?.orderId ?: CryptoOrderId.NONE)

        println(result)

        assertEquals(expected.ownerId, result.result?.ownerId)
        assertEquals(expected.orderState, result.result?.orderState)
        assertEquals(expected.orderType, result.result?.orderType)
        assertEquals(expected.pair, result.result?.pair)
        assertEquals(emptyList(), result.errors)
    }

    companion object : BaseInitOrder("create") {
        override val initObjects: List<CryptoOrder> = emptyList()

        private val created: Instant = Instant.nowMicros

        val order = createInitTestModel(
            CryptoUserId("test-owner-123-create"),
            CryptoOrderType.BUY,
            CryptoOrderState.ACTIVE,
            CryptoPair("BTC", "USD"),
            orderDate = created
        )
    }
}
