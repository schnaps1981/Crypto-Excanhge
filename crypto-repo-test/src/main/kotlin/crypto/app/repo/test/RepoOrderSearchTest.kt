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
            DbOrderFilterRequest(filter = CryptoFilterByDate(orderDateInstant)),
            listOf(initObjects[4], initObjects[9])
        )
    }

    @Test
    fun searchByState() {
        testFilter(
            DbOrderFilterRequest(filter = CryptoFilterByState(orderState)),
            listOf(initObjects[2], initObjects[7])
        )
    }

    @Test
    fun searchByType() {
        testFilter(
            DbOrderFilterRequest(filter = CryptoFilterByType(orderType)),
            listOf(initObjects[3], initObjects[8])
        )
    }

    @Test
    fun searchNotFound() {
        val result = runBlocking { repo.searchOrders(DbOrderFilterRequest(ownerId = notFoundOwnerId)) }

        assertEquals(false, result.isSuccess)
        assertEquals(null, result.result)
        assertEquals(
            listOf(CryptoError(field = "id", message = "Not Found")),
            result.errors
        )
    }

    private fun testFilter(filter: DbOrderFilterRequest, expected: List<CryptoOrder>) {
        val result = runBlocking { repo.searchOrders(filter) }
        assertEquals(true, result.isSuccess)

        assertEquals(expected, result.result)
        assertEquals(emptyList(), result.errors)
    }

    companion object : BaseInitOrder("search") {

        private val searchOwnerId = CryptoUserId("owner-124")
        private val orderDateInstant = Clock.System.now()
        private val orderState = CryptoOrderState.CANCELLED
        private val orderType = CryptoOrderType.SELL

        private val notFoundOwnerId = CryptoUserId("filter-not-found-by-owner")

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
