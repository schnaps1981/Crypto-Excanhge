package crypto.app.repo.test

import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import models.*
import models.filter.CryptoFilterByCurrency
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
        val expected = listOf(initObjects[1], initObjects[3])
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

        val expected = listOf(initObjects[2], initObjects[4]) //TODO обратить внимание
        assertEquals(expected, result.result?.sortedBy { it.orderId.asString() })
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun searchByDate() {

    }

    @Test
    fun searchByState() {

    }

    @Test
    fun searchByType() {

    }

    companion object : BaseInitOrder("search") {

        val searchOwnerId = CryptoUserId("owner-124")
        val mills = Clock.System.now()

        override val initObjects: List<CryptoOrder> = listOf(
            createInitTestModel(),
            createInitTestModel(ownerId = searchOwnerId),
            createInitTestModel(orderState = CryptoOrderState.CANCELLED),
            createInitTestModel(orderType = CryptoOrderType.SELL),
            createInitTestModel(orderDate = mills.toString()),
            createInitTestModel(orderPair = CryptoPair("BTC", "USD")),

            createInitTestModel(ownerId = searchOwnerId),
            createInitTestModel(orderState = CryptoOrderState.CANCELLED),
            createInitTestModel(orderType = CryptoOrderType.SELL),
            createInitTestModel(orderDate = mills.toString()),
            createInitTestModel(orderPair = CryptoPair("BTC", "ETH"))
        )
    }
}
