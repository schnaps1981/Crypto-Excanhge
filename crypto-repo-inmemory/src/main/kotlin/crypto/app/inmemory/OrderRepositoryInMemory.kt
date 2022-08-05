package crypto.app.inmemory

import crypto.app.inmemory.model.OrderEntity
import helpers.NONE
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
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
import java.util.*


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
            return DbOrderResponse(
                result = null, isSuccess = false, errors = listOf(
                    CryptoError(
                        field = "id",
                        message = "Id must not be null or empty",
                    )
                )
            )
        }
        cache.put(entity.orderId, entity)
        return DbOrderResponse(
            result = entity.toInternal(),
            isSuccess = true,
        )
    }

    private fun getOrRemoveById(id: CryptoOrderId, remove: Boolean = false): DbOrderResponse =
        if (id != CryptoOrderId.NONE) {
            cache.get(id.asString())?.let {
                if (remove) {
                    cache.remove(it.orderId)
                }

                DbOrderResponse(
                    result = it.toInternal(),
                    isSuccess = true,
                )

            } ?: DbOrderResponse(
                result = null, isSuccess = false, errors = listOf(
                    CryptoError(
                        field = "id",
                        message = "Not Found",
                    )
                )
            )
        } else {
            DbOrderResponse(
                result = null, isSuccess = false, errors = listOf(
                    CryptoError(
                        field = "id",
                        message = "Id must not be null or empty",
                    )
                )
            )
        }

    override suspend fun createOrder(request: DbOrderRequest): DbOrderResponse =
        save(
            request.order.copy(
                orderId = CryptoOrderId("${UUID.randomUUID()}"),
                created = if (request.order.created == Instant.NONE) Clock.System.now() else request.order.created
            )
        )

    override suspend fun deleteOrder(request: DbOrderIdRequest) = getOrRemoveById(request.id, true)

    override suspend fun readOrder(request: DbOrderIdRequest) = getOrRemoveById(request.id)

    override suspend fun updateOrder(request: DbOrderRequest): DbOrderResponse {
        val key = request.order.orderId.takeIf { it != CryptoOrderId.NONE }?.asString()
            ?: return DbOrderResponse(
                result = null,
                isSuccess = false,
                errors = listOf(
                    CryptoError(
                        field = "id",
                        message = "Id must not be null or blank"
                    )
                )
            )

        return if (cache.containsKey(key)) {
            save(request.order)
        } else {
            DbOrderResponse(
                result = null,
                isSuccess = false,
                errors = listOf(
                    CryptoError(
                        field = "id",
                        message = "Not Found"
                    )
                )
            )
        }
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
