package biz.order

import biz.helpers.principalAdmin
import biz.helpers.principalUser
import context.CryptoOrderContext
import crypto.app.inmemory.OrderRepositoryInMemory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import models.*
import models.commands.CryptoOrderCommands
import processors.CryptoOrderProcessor
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class RepoOrderDeleteTest {

    private val command = CryptoOrderCommands.DELETE
    private val uuidOld = "10000000-0000-0000-0000-000000000001"
    private val uuidBad = "10000000-0000-0000-0000-000000000002"
    private val orderIdSuccessDel = CryptoOrderId("order-id-123")

    private val initOrder = CryptoOrder(
        orderId = orderIdSuccessDel,
        ownerId = CryptoUserId("user-123"),
        quantity = "1.4".toBigDecimal(),
        price = "100.3".toBigDecimal(),
        amount = "1".toBigDecimal(),
        orderType = CryptoOrderType.SELL,
        orderState = CryptoOrderState.ACTIVE,
        pair = CryptoPair(first = "BTC", second = "USD"),
        lock = CryptoLock(uuidOld)
    )

    private val settings by lazy {
        CryptoSettings(
            repoTest = OrderRepositoryInMemory(initObjects = listOf(initOrder))
        )
    }

    private val processor = CryptoOrderProcessor(settings)

    @Test
    fun repoDeleteOrderSuccessTest() = runTest {
        val orderToDelete = CryptoOrder(
            orderId = orderIdSuccessDel,
            lock = CryptoLock(uuidOld)
        )

        val context = CryptoOrderContext(
            command = command,
            state = CryptoState.NONE,
            workMode = CryptoWorkMode.TEST,
            principal = principalUser(id = initOrder.ownerId),
            orderRequest = orderToDelete,
        )

        processor.exec(context)
        println(context)

        assertEquals(CryptoState.FINISHING, context.state)
        assertTrue(context.errors.isEmpty())
        assertEquals(context.orderResponse.orderState, CryptoOrderState.NONE)
        assertEquals(uuidOld, context.orderResponse.lock.asString())
    }

    @Test
    fun repoDeleteConcurrentTest() = runTest {
        val orderToDelete = CryptoOrder(
            orderId = orderIdSuccessDel,
            lock = CryptoLock(uuidBad)
        )

        val context = CryptoOrderContext(
            command = command,
            state = CryptoState.NONE,
            principal = principalUser(id = initOrder.ownerId),
            workMode = CryptoWorkMode.TEST,
            orderRequest = orderToDelete,
        )

        processor.exec(context)
        println(context)

        assertEquals(CryptoState.FAILED, context.state)
        assertEquals(1, context.errors.size)
        assertEquals("lock", context.errors.first().field)
    }

    @Test
    fun repoDeleteNotFoundTest() = runTest {
        val ctx = CryptoOrderContext(
            command = command,
            state = CryptoState.NONE,
            workMode = CryptoWorkMode.TEST,
            principal = principalUser(id = initOrder.ownerId),
            orderRequest = CryptoOrder(
                orderId = CryptoOrderId("12345"),
                lock = CryptoLock(uuidOld)
            )
        )

        processor.exec(ctx)

        println(ctx)

        assertEquals(CryptoState.FAILED, ctx.state)
        assertEquals(CryptoOrder(), ctx.orderResponse)
        assertEquals(1, ctx.errors.size)
        assertEquals("id", ctx.errors.first().field)
    }

    @Test
    fun repoDeleteOrderSuccessByAdminTest() = runTest {
        val orderToDelete = CryptoOrder(
            orderId = orderIdSuccessDel,
            lock = CryptoLock(uuidOld),
            ownerId = CryptoUserId("created-by-user")
        )

        val context = CryptoOrderContext(
            command = command,
            state = CryptoState.NONE,
            workMode = CryptoWorkMode.TEST,
            principal = principalAdmin(id = CryptoUserId("im-admin")),
            orderRequest = orderToDelete,
        )

        processor.exec(context)
        println(context)

        assertEquals(CryptoState.FINISHING, context.state)
        assertTrue(context.errors.isEmpty())
        assertEquals(context.orderResponse.orderState, CryptoOrderState.NONE)
        assertEquals(uuidOld, context.orderResponse.lock.asString())
    }

    @Test
    fun repoDeleteOrderMustFailByBannedUserTest() = runTest {
        val orderToDelete = CryptoOrder(
            orderId = orderIdSuccessDel,
            lock = CryptoLock(uuidOld)
        )

        val context = CryptoOrderContext(
            command = command,
            state = CryptoState.NONE,
            workMode = CryptoWorkMode.TEST,
            principal = principalUser(id = initOrder.ownerId, banned = true),
            orderRequest = orderToDelete,
        )

        processor.exec(context)
        println(context)

        assertEquals(CryptoState.FAILED, context.state)
        assertTrue(context.errors.isNotEmpty())
        assertEquals("User is not allowed to this operation", context.errors.firstOrNull()?.message)
    }
}
