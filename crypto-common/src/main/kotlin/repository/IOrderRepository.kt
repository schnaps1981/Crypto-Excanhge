package repository

import java.util.*

interface IOrderRepository {
    suspend fun createOrder(request: DbOrderRequest): DbOrderResponse
    suspend fun deleteOrder(request: DbOrderIdRequest): DbOrderResponse
    suspend fun readOrder(request: DbOrderIdRequest): DbOrderResponse
    suspend fun updateOrder(request: DbOrderRequest): DbOrderResponse
    suspend fun searchOrders(request: DbOrderFilterRequest): DbOrdersResponse

    val uuid: String
        get() = "${UUID.randomUUID()}"
}
