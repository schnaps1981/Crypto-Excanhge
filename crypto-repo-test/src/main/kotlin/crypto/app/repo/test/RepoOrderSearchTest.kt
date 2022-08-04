package crypto.app.repo.test

import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import models.*
import models.filter.CryptoFilterByCurrency
import models.filter.CryptoFilterByDate
import models.filter.CryptoFilterByState
import models.filter.CryptoFilterByType
import org.junit.Test
import repository.DbOrderFilterRequest
import repository.IOrderRepository
import kotlin.test.assertEquals

abstract class RepoOrderSearchTest {
    abstract val repo: IOrderRepository

    @Test
    fun searchOwner() {
        val result = runBlocking { repo.searchOrders(DbOrderFilterRequest(ownerId = searchOwnerId)) }
        assertEquals(true, result.isSuccess)
        val expected = listOf(initObjects[1], initObjects[6])
        assertEquals(expected, result.result?.sortedBy { it.orderId.asString() })
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun searchNotFound() {

    }

    @Test
    fun searchByCurrency() {
        val result = runBlocking { repo.searchOrders(DbOrderFilterRequest(filter = CryptoFilterByCurrency("BTC"))) }
        assertEquals(true, result.isSuccess)

        val expected = listOf(initObjects[5], initObjects[10])
        assertEquals(expected, result.result)
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun searchByDate() {
        val result =
            runBlocking { repo.searchOrders(DbOrderFilterRequest(filter = CryptoFilterByDate(orderDateInstant))) }
        assertEquals(true, result.isSuccess)

        val expected = listOf(initObjects[4], initObjects[9])
        assertEquals(expected, result.result)
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun searchByState() {
        val result = runBlocking { repo.searchOrders(DbOrderFilterRequest(filter = CryptoFilterByState(orderState))) }
        assertEquals(true, result.isSuccess)

        val expected = listOf(initObjects[4], initObjects[9])
        assertEquals(expected, result.result)
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun searchByType() {
        val result = runBlocking {
            repo.searchOrders(DbOrderFilterRequest(filter = CryptoFilterByType(orderType)))
        }
        assertEquals(true, result.isSuccess)

        val expected = listOf(initObjects[4], initObjects[9])
        assertEquals(expected, result.result)
        assertEquals(emptyList(), result.errors)
    }

    companion object : BaseInitOrder("search") {

        private val searchOwnerId = CryptoUserId("owner-124")
        private val orderDateInstant = Clock.System.now()
        private val orderState = CryptoOrderState.CANCELLED
        private val orderType = CryptoOrderType.SELL

        override val initObjects: List<CryptoOrder> = listOf(
            createInitTestModel(),
            createInitTestModel(ownerId = searchOwnerId),
            createInitTestModel(orderState = orderState),
            createInitTestModel(orderType = orderType),
            createInitTestModel(orderDate = orderDateInstant),
            createInitTestModel(orderPair = CryptoPair("BTC", "USD")),
            createInitTestModel(ownerId = searchOwnerId),
            createInitTestModel(orderState = orderState),
            createInitTestModel(orderType = orderType),
            createInitTestModel(orderDate = orderDateInstant),
            createInitTestModel(orderPair = CryptoPair("BTC", "ETH"))
        )
    }
}
