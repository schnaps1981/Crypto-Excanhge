package biz.order

import biz.helpers.principalAdmin
import biz.helpers.principalUser
import context.CryptoOrderContext
import crypto.app.inmemory.OrderRepositoryInMemory
import crypto.app.repo.test.BaseInitOrder
import helpers.isEqualIgnoreOrder
import helpers.nowMicros
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import models.*
import models.commands.CryptoOrderCommands
import models.filter.*
import org.junit.Test
import processors.CryptoOrderProcessor
import kotlin.test.assertEquals
import kotlin.test.assertTrue


@OptIn(ExperimentalCoroutinesApi::class)
class RepoOrderReadTest {

    private val command = CryptoOrderCommands.READ

    private val settings by lazy {
        CryptoSettings(
            repoTest = OrderRepositoryInMemory(initObjects = initObjects)
        )
    }

    private val processor = CryptoOrderProcessor(settings)

    @Test
    fun `read orders by user with no filter`() = runTest {
        val expected = listOf(initObjects[1], initObjects[2], initObjects[3], initObjects[4])
        val filter = ICryptoFilter.NONE

        testFilter(ownerUserId, expected, filter)
    }

    @Test

    fun `read orders by admin with no filter`() = runTest {
        val expected = initObjects
        val filter = ICryptoFilter.NONE

        testFilter(ownerAdminId, expected, filter)
    }

    @Test
    fun `read orders by user with currency filter`() = runTest {
        val filter = CryptoFilterByCurrency(readByCurrency)
        val expected = listOf(initObjects[4])

        testFilter(ownerUserId, expected, filter)
    }

    @Test
    fun `read orders by admin with currency filter`() = runTest {
        val filter = CryptoFilterByCurrency(readByCurrency)
        val expected = listOf(initObjects[4], initObjects[8])

        testFilter(ownerAdminId, expected, filter)
    }

    @Test
    fun `read orders by user with date filter`() = runTest {
        val filter = CryptoFilterByDate(created)
        val expected = listOf(initObjects[3])

        testFilter(ownerUserId, expected, filter)
    }

    @Test
    fun `read orders by admin with date filter`() = runTest {
        val filter = CryptoFilterByDate(created)
        val expected = listOf(initObjects[3], initObjects[7])

        testFilter(ownerAdminId, expected, filter)
    }

    @Test
    fun `read orders by user with state filter`() = runTest {
        val filter = CryptoFilterByState(readByOrderState)
        val expected = listOf(initObjects[1])

        testFilter(ownerUserId, expected, filter)
    }

    @Test
    fun `read orders by admin with state filter`() = runTest {
        val filter = CryptoFilterByState(readByOrderState)
        val expected = listOf(initObjects[1], initObjects[5])

        testFilter(ownerAdminId, expected, filter)
    }

    @Test
    fun `read orders by user with type filter`() = runTest {
        val filter = CryptoFilterByType(readByOrderType)
        val expected = listOf(initObjects[2])

        testFilter(ownerUserId, expected, filter)
    }

    @Test
    fun `read orders by admin with type filter`() = runTest {
        val filter = CryptoFilterByType(readByOrderType)
        val expected = listOf(initObjects[2], initObjects[6])

        testFilter(ownerAdminId, expected, filter)
    }

    private suspend fun testFilter(owner: CryptoUserId, expected: List<CryptoOrder>, filter: ICryptoFilter) {
        val context = CryptoOrderContext(
            command = command,
            state = CryptoState.NONE,
            principal = if (owner == ownerUserId) principalUser(owner) else principalAdmin(owner),
            workMode = CryptoWorkMode.TEST,
            orderFilter = filter
        )

        processor.exec(context)

        println(context)

        println("Expected: $expected")
        println("Result: ${context.ordersResponse}")
        println("Result size: ${context.ordersResponse.size}")

        assertEquals(CryptoState.FINISHING, context.state)
        assertTrue(expected isEqualIgnoreOrder context.ordersResponse)
    }

    companion object : BaseInitOrder("read") {
        private val created: Instant = Instant.nowMicros

        private val readByOrderState = CryptoOrderState.CANCELLED
        private val readByOrderType = CryptoOrderType.SELL
        private const val readByCurrency = "BTC"

        private val ownerUserId = CryptoUserId("owner-user")
        private val ownerAdminId = CryptoUserId("owner-admin")

        override val initObjects: List<CryptoOrder> = listOf(
            createInitTestModel(orderId = "ord-0"),

            createInitTestModel(ownerId = ownerUserId, orderId = "ord-1", orderState = readByOrderState),
            createInitTestModel(ownerId = ownerUserId, orderId = "ord-2", orderType = readByOrderType),
            createInitTestModel(ownerId = ownerUserId, orderId = "ord-3", orderDate = created),
            createInitTestModel(ownerId = ownerUserId, orderId = "ord-4", orderPair = CryptoPair("BTC", "USD")),

            createInitTestModel(ownerId = ownerAdminId, orderId = "ord-5", orderState = readByOrderState),
            createInitTestModel(ownerId = ownerAdminId, orderId = "ord-6", orderType = readByOrderType),
            createInitTestModel(ownerId = ownerAdminId, orderId = "ord-7", orderDate = created),
            createInitTestModel(ownerId = ownerAdminId, orderId = "ord-8", orderPair = CryptoPair("ETH", "BTC"))
        )
    }
}
