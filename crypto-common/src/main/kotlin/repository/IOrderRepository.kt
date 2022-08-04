package repository

interface IOrderRepository {
    suspend fun createOrder(order: DbOrderRequest): DbOrderResponse
    suspend fun deleteOrder(order: DbOrderIdRequest): DbOrderResponse
    suspend fun readOrder(order: DbOrderIdRequest): DbOrderResponse
    suspend fun updateOrder(order: DbOrderRequest): DbOrderResponse
    suspend fun searchOrders(order: DbOrderFilterRequest): DbOrdersResponse

}
