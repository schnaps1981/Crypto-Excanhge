package biz.order

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

    private val repo = OrderRepositoryInMemory(initObjects = initObjects)
    private val processor = CryptoOrderProcessor()

    @Test
    fun repoReadSuccessTest() = runTest {
        val expected = initObjects
        val filter = ICryptoFilter.NONE

        testFilter(expected, filter)
    }

    @Test
    fun readOrdersByFilterCurrency() = runTest {
        val filter = CryptoFilterByCurrency(readByCurrency)
        val expected = listOf(initObjects[4], initObjects[8])

        testFilter(expected, filter)
    }

    @Test
    fun readOrdersByFilterDate() = runTest {
        val filter = CryptoFilterByDate(created)
        val expected = listOf(initObjects[3], initObjects[7])

        testFilter(expected, filter)
    }

    @Test
    fun readOrdersByFilterState() = runTest {
        val filter = CryptoFilterByState(readByOrderState)
        val expected = listOf(initObjects[1], initObjects[5])

        testFilter(expected, filter)
    }

    @Test
    fun readOrdersByFilterType() = runTest {
        val filter = CryptoFilterByType(readByOrderType)
        val expected = listOf(initObjects[2], initObjects[6])

        testFilter(expected, filter)
    }


    private suspend fun testFilter(expected: List<CryptoOrder>, filter: ICryptoFilter) {
        val context = CryptoOrderContext(
            command = command,
            orderRepo = repo,
            state = CryptoState.NONE,
            workMode = CryptoWorkMode.TEST,
            orderFilter = filter
        )

        processor.exec(context)

        println(context)

        assertEquals(CryptoState.FINISHING, context.state)
        assertTrue(expected isEqualIgnoreOrder context.ordersResponse)
    }

    companion object : BaseInitOrder("read") {
        private val created: Instant = Instant.nowMicros

        private val readByOrderState = CryptoOrderState.CANCELLED
        private val readByOrderType = CryptoOrderType.SELL
        private const val readByCurrency = "BTC"

        override val initObjects: List<CryptoOrder> = listOf(
            createInitTestModel(orderId = "ord-0"),
            createInitTestModel(orderId = "ord-1", orderState = readByOrderState),
            createInitTestModel(orderId = "ord-2", orderType = readByOrderType),
            createInitTestModel(orderId = "ord-3", orderDate = created),
            createInitTestModel(orderId = "ord-4", orderPair = CryptoPair("BTC", "USD")),
            createInitTestModel(orderId = "ord-5", orderState = readByOrderState),
            createInitTestModel(orderId = "ord-6", orderType = readByOrderType),
            createInitTestModel(orderId = "ord-7", orderDate = created),
            createInitTestModel(orderId = "ord-8", orderPair = CryptoPair("ETH", "BTC"))
        )
    }
}
