package crypto.app.repo.test

import helpers.isEqualIgnoreOrder
import helpers.nowMicros
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Instant
import models.*
import models.filter.CryptoFilterByCurrency
import models.filter.CryptoFilterByDate
import models.filter.CryptoFilterByState
import models.filter.CryptoFilterByType
import org.junit.Test
import repository.DbOrderFilterRequest
import repository.IOrderRepository
import kotlin.test.assertEquals
import kotlin.test.assertTrue

abstract class RepoOrderSearchTest {
    abstract val repo: IOrderRepository

    @Test
    fun searchOwner() {
        testFilter(
            DbOrderFilterRequest(ownerId = searchOwnerId),
            listOf(initObjects[1], initObjects[6])
        )
    }

    @Test
    fun searchByCurrency() {
        testFilter(
            DbOrderFilterRequest(filter = CryptoFilterByCurrency("BTC")),
            listOf(initObjects[5], initObjects[10])
        )
    }

    @Test
    fun searchByDate() {
        testFilter(
            DbOrderFilterRequest(filter = CryptoFilterByDate(created)),
            listOf(initObjects[4], initObjects[9])
        )
    }

    @Test
    fun searchByState() {
        testFilter(
            DbOrderFilterRequest(filter = CryptoFilterByState(searchOrderState)),
            listOf(initObjects[2], initObjects[7])
        )
    }

    @Test
    fun searchByType() {
        testFilter(
            DbOrderFilterRequest(filter = CryptoFilterByType(searchOrderType)),
            listOf(initObjects[3], initObjects[8])
        )
    }

    @Test
    fun searchNotFound() {
        val result = runBlocking { repo.searchOrders(DbOrderFilterRequest(ownerId = notFoundOwnerId)) }

        println(result)

        assertEquals(true, result.isSuccess)
        assertEquals(emptyList(), result.result)
        assertEquals(emptyList(), result.errors)
    }

    private fun testFilter(filter: DbOrderFilterRequest, expected: List<CryptoOrder>) {
        val result = runBlocking { repo.searchOrders(filter) }

        println("Expected\n $expected")
        println("Result\n $result")

        assertEquals(true, result.isSuccess)

        val equal = expected isEqualIgnoreOrder result.result.orEmpty()

        assertTrue(equal)
        assertEquals(emptyList(), result.errors)
    }

    companion object : BaseInitOrder("search") {

        private val created: Instant = Instant.nowMicros

        private val searchOwnerId = CryptoUserId("owner-124")
        private val searchOrderState = CryptoOrderState.CANCELLED
        private val searchOrderType = CryptoOrderType.SELL

        private val notFoundOwnerId = CryptoUserId("filter-not-found-by-owner")

        override val initObjects: List<CryptoOrder> = listOf(
            createInitTestModel(orderId = "ord-1"),
            createInitTestModel(orderId = "ord-2", ownerId = searchOwnerId),
            createInitTestModel(orderId = "ord-3", orderState = searchOrderState),
            createInitTestModel(orderId = "ord-4", orderType = searchOrderType),
            createInitTestModel(orderId = "ord-5", orderDate = created),
            createInitTestModel(orderId = "ord-6", orderPair = CryptoPair("BTC", "USD")),
            createInitTestModel(orderId = "ord-7", ownerId = searchOwnerId),
            createInitTestModel(orderId = "ord-8", orderState = searchOrderState),
            createInitTestModel(orderId = "ord-9", orderType = searchOrderType),
            createInitTestModel(orderId = "ord-10", orderDate = created),
            createInitTestModel(orderId = "ord-11", orderPair = CryptoPair("ETH", "BTC"))
        )
    }
}
