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
    fun searchByUserByCurrency() {
        testFilter(
            DbOrderFilterRequest(
                ownerId = ownerUserId,
                filter = CryptoFilterByCurrency(
                    ticker = "BTC",
                    filterPermissions = mutableSetOf(CryptoFilterApplyTo.OWN)
                )
            ),
            listOf(initObjects[5])
        )
    }

    @Test
    fun searchByAdminByCurrency() {
        testFilter(
            DbOrderFilterRequest(
                ownerId = ownerAdminId,
                filter = CryptoFilterByCurrency(
                    ticker = "BTC",
                    filterPermissions = mutableSetOf(CryptoFilterApplyTo.ANY)
                )
            ),
            listOf(initObjects[5], initObjects[10])
        )
    }

    @Test
    fun searchByUserByDate() {
        testFilter(
            DbOrderFilterRequest(
                ownerId = ownerUserId,
                filter = CryptoFilterByDate(
                    orderDate = created,
                    filterPermissions = mutableSetOf(CryptoFilterApplyTo.OWN)
                )
            ),
            listOf(initObjects[4])
        )
    }

    @Test
    fun searchByAdminByDate() {
        testFilter(
            DbOrderFilterRequest(
                ownerId = ownerAdminId,
                filter = CryptoFilterByDate(
                    orderDate = created,
                    filterPermissions = mutableSetOf(CryptoFilterApplyTo.ANY)
                )
            ),
            listOf(initObjects[4], initObjects[9])
        )
    }

    @Test
    fun searchByUserByState() {
        testFilter(
            DbOrderFilterRequest(
                ownerId = ownerUserId,
                filter = CryptoFilterByState(
                    orderState = searchOrderState,
                    filterPermissions = mutableSetOf(CryptoFilterApplyTo.OWN)
                )
            ),

            listOf(initObjects[2])
        )
    }

    @Test
    fun searchByAdminByState() {
        testFilter(
            DbOrderFilterRequest(
                ownerId = ownerAdminId,
                filter = CryptoFilterByState(
                    orderState = searchOrderState,
                    filterPermissions = mutableSetOf(CryptoFilterApplyTo.ANY)
                )
            ),

            listOf(initObjects[2], initObjects[7])
        )
    }

    @Test
    fun searchByUserByType() {
        testFilter(
            DbOrderFilterRequest(
                ownerId = ownerUserId,
                filter = CryptoFilterByType(
                    orderType = searchOrderType,
                    filterPermissions = mutableSetOf(CryptoFilterApplyTo.OWN)
                )
            ),
            listOf(initObjects[3])
        )
    }

    @Test
    fun searchByAdminByType() {
        testFilter(
            DbOrderFilterRequest(
                ownerId = ownerAdminId,
                filter = CryptoFilterByType(
                    orderType = searchOrderType,
                    filterPermissions = mutableSetOf(CryptoFilterApplyTo.ANY)
                )
            ),
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
        println("Result size: ${result.result?.size}")

        assertEquals(true, result.isSuccess)

        val equal = expected isEqualIgnoreOrder result.result.orEmpty()

        assertTrue(equal)
        assertEquals(emptyList(), result.errors)
    }

    companion object : BaseInitOrder("search") {

        private val created: Instant = Instant.nowMicros

        private val ownerUserId = CryptoUserId("owner-user")
        private val ownerAdminId = CryptoUserId("owner-admin")

        private val searchOrderState = CryptoOrderState.CANCELLED
        private val searchOrderType = CryptoOrderType.SELL

        private val notFoundOwnerId = CryptoUserId("filter-not-found-by-owner")

        override val initObjects: List<CryptoOrder> = listOf(
            createInitTestModel(orderId = "ord-1"),
            createInitTestModel(ownerId = ownerUserId, orderId = "ord-2"),
            createInitTestModel(ownerId = ownerUserId, orderId = "ord-3", orderState = searchOrderState),
            createInitTestModel(ownerId = ownerUserId, orderId = "ord-4", orderType = searchOrderType),
            createInitTestModel(ownerId = ownerUserId, orderId = "ord-5", orderDate = created),
            createInitTestModel(ownerId = ownerUserId, orderId = "ord-6", orderPair = CryptoPair("BTC", "USD")),

            createInitTestModel(ownerId = ownerAdminId, orderId = "ord-7"),
            createInitTestModel(ownerId = ownerAdminId, orderId = "ord-8", orderState = searchOrderState),
            createInitTestModel(ownerId = ownerAdminId, orderId = "ord-9", orderType = searchOrderType),
            createInitTestModel(ownerId = ownerAdminId, orderId = "ord-10", orderDate = created),
            createInitTestModel(ownerId = ownerAdminId, orderId = "ord-11", orderPair = CryptoPair("ETH", "BTC"))
        )
    }
}
