import helpers.*
import kotlinx.datetime.Instant
import models.*
import models.filter.CryptoFilterByCurrency
import models.filter.CryptoFilterByDate
import models.filter.CryptoFilterByState
import models.filter.CryptoFilterByType
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import repository.*
import java.sql.SQLException
import java.util.*

class OrderRepositorySql(
    config: SQLDbConfig, initObjects: Collection<CryptoOrder> = emptyList()
) : IOrderRepository {

    private val db by lazy {
        SqlConnector(config).connect(OrderTable, PairTable)
    }

    init {
        initObjects.forEach {
            save(it)
        }
    }

    override suspend fun createOrder(request: DbOrderRequest): DbOrderResponse {
        val order = request.order.copy(created = Instant.nowMicros, lock = CryptoLock(UUID.randomUUID().toString()))
        return save(order)

    }

    override suspend fun deleteOrder(request: DbOrderIdRequest): DbOrderResponse {
        val key = request.id.takeIf { it != CryptoOrderId.NONE }?.asString() ?: return resultErrorIdIsEmpty

        return safeTransaction({
            val local = OrderTable.select { OrderTable.orderId.eq(key) }.single().let { OrderTable.from(it) }

            if (local.lock == request.lock) {
                val deletedCount = OrderTable.deleteWhere { OrderTable.orderId.eq(key) }

                if (deletedCount == 1) {
                    DbOrderResponse(result = local.copy(orderState = CryptoOrderState.NONE), isSuccess = true)
                } else {
                    DbOrderResponse.withErrorMessage("Not deleted")
                }
            } else {
                resultErrorConcurrent
            }
        }, {
            resultErrorIdNotFound
        })
    }

    override suspend fun readOrder(request: DbOrderIdRequest): DbOrderResponse {
        val key = request.id.takeIf { it != CryptoOrderId.NONE }?.asString() ?: return resultErrorIdIsEmpty

        return safeTransaction({
            val result = OrderTable.select { OrderTable.orderId.eq(key) }.single()

            DbOrderResponse(OrderTable.from(result), true)
        }, {
            when (this) {
                is NoSuchElementException -> resultErrorIdNotFound
                is IllegalArgumentException -> DbOrderResponse.withErrorMessage("More than one element with the same id")
                else -> DbOrderResponse.withErrorMessage(message = localizedMessage)
            }
        })
    }

    override suspend fun updateOrder(request: DbOrderRequest): DbOrderResponse {
        val key = request.order.orderId.takeIf { it != CryptoOrderId.NONE }?.asString() ?: return resultErrorIdIsEmpty
        val oldLock = request.order.lock.takeIf { it != CryptoLock.NONE }?.asString()

        return safeTransaction({
            val oldOrder = OrderTable.select { OrderTable.orderId.eq(key) }.singleOrNull()?.let {
                OrderTable.from(it)
            } ?: return@safeTransaction resultErrorIdNotFound

            val newOrder = oldOrder.toNewOrder(request.order)

            return@safeTransaction when (oldLock) {
                null, oldOrder.lock.asString() -> updateDb(newOrder)
                else -> resultErrorConcurrent
            }
        }, {
            DbOrderResponse.withErrorMessage(message ?: localizedMessage)
        })
    }

    override suspend fun searchOrders(request: DbOrderFilterRequest): DbOrdersResponse {
        return safeTransaction({

            val results = (OrderTable innerJoin PairTable).select {
                (request.filter.filterPermissions.map {
                    when (it) {
                        CryptoFilterApplyTo.ANY -> Op.TRUE

                        CryptoFilterApplyTo.OWN -> OrderTable.ownerId eq request.ownerId.asString()

                        CryptoFilterApplyTo.NONE -> Op.FALSE
                    }
                }.compoundAnd()) and ((request.filter as? CryptoFilterByType)?.let {
                    OrderTable.orderType eq it.orderType
                } ?: Op.TRUE) and ((request.filter as? CryptoFilterByState)?.let {
                    OrderTable.orderState eq it.orderState
                } ?: Op.TRUE) and ((request.filter as? CryptoFilterByDate)?.orderDate?.takeIf { it != Instant.NONE }
                    ?.let {

                        val filterDateStart = it.toBeginDay()
                        val filterDateEnd = it.toEndDay()

                        OrderTable.created.greaterEq(filterDateStart) and OrderTable.created.less(filterDateEnd)

                    } ?: Op.TRUE) and ((request.filter as? CryptoFilterByCurrency)?.ticker?.takeIf { it.isNotBlank() }
                    ?.let {
                        ((PairTable.first eq it) or (PairTable.second eq it))
                    } ?: Op.TRUE)
            }

            DbOrdersResponse(result = results.map { OrderTable.from(it) }, isSuccess = true)

        }, {
            DbOrdersResponse.withErrorMessage(message ?: localizedMessage)
        })
    }

    private fun save(item: CryptoOrder): DbOrderResponse {
        return safeTransaction({
            val pairDb = PairTable.insertAndGetId {
                it[first] = item.pair.first
                it[second] = item.pair.second
            }

            val res = OrderTable.insert {
                it[orderId] = item.orderId.takeIf { it != CryptoOrderId.NONE }?.asString() ?: uuid
                it[ownerId] = item.ownerId.asString()
                it[created] = item.created
                it[orderState] = item.orderState
                it[amount] = item.amount.toString()
                it[quantity] = item.quantity.toString()
                it[price] = item.price.toString()
                it[orderType] = item.orderType
                it[pair] = pairDb
                it[lock] = item.lock.asString()
            }

            DbOrderResponse(OrderTable.from(res), true)
        }, {
            DbOrderResponse.withErrorMessage(message ?: localizedMessage)
        })
    }

    private fun updateDb(newOrder: CryptoOrder): DbOrderResponse {

        val idUpdate = newOrder.orderId.asString()

        OrderTable.update({ OrderTable.orderId.eq(idUpdate) }) {
            it[orderId] = newOrder.orderId.asString()
            it[ownerId] = newOrder.ownerId.asString()
            it[created] = newOrder.created
            it[orderState] = newOrder.orderState
            it[amount] = newOrder.amount.toString()
            it[quantity] = newOrder.quantity.toString()
            it[price] = newOrder.price.toString()
            it[orderType] = newOrder.orderType
            it[lock] = newOrder.lock.asString()
        }
        val result = OrderTable.select { OrderTable.orderId.eq(idUpdate) }.single()

        return DbOrderResponse(result = OrderTable.from(result), isSuccess = true)
    }

    private fun <T> safeTransaction(statement: Transaction.() -> T, handleException: Throwable.() -> T): T {
        return try {
            transaction(db, statement)
        } catch (e: SQLException) {
            throw e
        } catch (e: Throwable) {
            return handleException(e)
        }
    }

    private fun CryptoOrder.toNewOrder(newOrder: CryptoOrder) =
        CryptoOrder(orderId = newOrder.orderId.takeIf { it != CryptoOrderId.NONE } ?: this.orderId,
            ownerId = newOrder.ownerId.takeIf { it != CryptoUserId.NONE } ?: this.ownerId,
            created = newOrder.created.takeIf { it != Instant.NONE } ?: this.created,
            orderState = newOrder.orderState.takeIf { it != CryptoOrderState.NONE } ?: this.orderState,
            amount = newOrder.amount.takeIf { it != CryptoOrder.ZERO } ?: this.amount,
            quantity = newOrder.quantity.takeIf { it != CryptoOrder.ZERO } ?: this.quantity,
            price = newOrder.price.takeIf { it != CryptoOrder.ZERO } ?: this.price,
            orderType = newOrder.orderType.takeIf { it != CryptoOrderType.NONE } ?: this.orderType,
            pair = newOrder.pair.takeIf { it != CryptoPair.EMPTY } ?: this.pair,
            lock = CryptoLock("${UUID.randomUUID()}"))
}
