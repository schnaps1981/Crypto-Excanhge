package crypto.app.inmemory

import crypto.app.inmemory.model.OrderEntity
import helpers.*
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone.Companion.currentSystemDefault
import kotlinx.datetime.toLocalDateTime
import models.*
import models.filter.CryptoFilterByCurrency
import models.filter.CryptoFilterByDate
import models.filter.CryptoFilterByState
import models.filter.CryptoFilterByType
import org.ehcache.Cache
import org.ehcache.CacheManager
import org.ehcache.config.builders.CacheConfigurationBuilder
import org.ehcache.config.builders.CacheManagerBuilder
import org.ehcache.config.builders.ExpiryPolicyBuilder
import org.ehcache.config.builders.ResourcePoolsBuilder
import repository.*
import java.time.Duration


class OrderRepositoryInMemory(
    private val initObjects: List<CryptoOrder> = emptyList(), private val ttl: Duration = Duration.ofMinutes(2)
) : IOrderRepository {
    private val cache: Cache<String, OrderEntity> = let {
        val cacheManager: CacheManager = CacheManagerBuilder.newCacheManagerBuilder().build(true)

        cacheManager.createCache(
            "order-cache", CacheConfigurationBuilder.newCacheConfigurationBuilder(
                String::class.java, OrderEntity::class.java, ResourcePoolsBuilder.heap(100)
            ).withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(ttl)).build()
        )
    }

    private val mutex = Mutex()

    init {
        runBlocking {
            initObjects.forEach {
                save(it)
            }
        }
    }

    private fun save(order: CryptoOrder): DbOrderResponse {
        val entity = OrderEntity(order)

        if (entity.orderId == null) {
            return resultErrorIdIsEmpty
        }

        cache.put(entity.orderId, entity)

        return DbOrderResponse(
            result = entity.toInternal(),
            isSuccess = true,
        )
    }

    override suspend fun createOrder(request: DbOrderRequest): DbOrderResponse {
        val key = uuid

        val order = request.order.copy(
            orderId = CryptoOrderId(key),
            created = if (request.order.created == Instant.NONE) Instant.nowMicros else request.order.created,
            lock = CryptoLock(uuid),
            orderState = CryptoOrderState.ACTIVE
        )

        val entity = OrderEntity(order)

        mutex.withLock {
            cache.put(key, entity)
        }

        return DbOrderResponse(
            result = order,
            isSuccess = true
        )
    }


    override suspend fun deleteOrder(request: DbOrderIdRequest): DbOrderResponse {
        val key = request.id.takeIf { it != CryptoOrderId.NONE }?.asString() ?: return resultErrorIdIsEmpty

        mutex.withLock {
            val local = cache.get(key) ?: return resultErrorIdNotFound

            if (local.lock == null || local.lock == request.lock.asString()) {
                cache.remove(key)

                return DbOrderResponse(
                    result = local.toInternal().copy(orderState = CryptoOrderState.NONE),
                    isSuccess = true,
                    errors = emptyList()
                )
            } else {
                return resultErrorConcurrent
            }
        }
    }

    override suspend fun readOrder(request: DbOrderIdRequest): DbOrderResponse {
        val key = request.id.takeIf { it != CryptoOrderId.NONE }?.asString() ?: return resultErrorIdIsEmpty

        return cache.get(key)?.let {
            DbOrderResponse(
                result = it.toInternal(),
                isSuccess = true
            )
        } ?: resultErrorIdNotFound
    }

    override suspend fun updateOrder(request: DbOrderRequest): DbOrderResponse {
        val key = request.order.orderId.takeIf { it != CryptoOrderId.NONE }?.asString() ?: return resultErrorIdIsEmpty
        val oldLock = request.order.lock.takeIf { it != CryptoLock.NONE }?.asString()
        val newOrder = request.order.copy(lock = CryptoLock(uuid))
        val entity = OrderEntity(newOrder)

        mutex.withLock {
            val local = cache.get(key)

            when {
                local == null -> return resultErrorIdNotFound
                local.lock == null || local.lock == oldLock -> cache.put(key, entity)
                else -> return resultErrorConcurrent
            }
        }

        return DbOrderResponse(
            result = newOrder,
            isSuccess = true
        )
    }

    override suspend fun searchOrders(request: DbOrderFilterRequest): DbOrdersResponse {
        val result = cache.asFlow()
            .filter { entry ->
                request.ownerId.takeIf { it != CryptoUserId.NONE }?.let {
                    it.asString() == entry.value.ownerId
                } ?: true
            }
            .filter { entry ->
                (request.filter as? CryptoFilterByCurrency)?.ticker?.takeIf { it.isNotBlank() }?.let {
                    entry.value.pair?.first == it || entry.value.pair?.second == it
                } ?: true
            }
            .filter { entry ->
                (request.filter as? CryptoFilterByDate)?.orderDate?.takeIf { it != Instant.NONE }?.let {
                    val filterDate = it.toLocalDateTime(currentSystemDefault()).date
                    val entryDate =
                        Instant.parse(entry.value.created ?: "").toLocalDateTime(currentSystemDefault()).date

                    entryDate.compareTo(filterDate) == 0
                } ?: true
            }
            .filter { entry ->
                (request.filter as? CryptoFilterByState)?.orderState.takeIf { it != CryptoOrderState.NONE }?.let {
                    it.name == entry.value.orderState
                } ?: true
            }
            .filter { entry ->
                (request.filter as? CryptoFilterByType)?.orderType.takeIf { it != CryptoOrderType.NONE }?.let {
                    it.name == entry.value.orderType
                } ?: true
            }
            .map { it.value.toInternal() }
            .toList()

        return DbOrdersResponse(
            result = result,
            isSuccess = true
        )
    }
}
