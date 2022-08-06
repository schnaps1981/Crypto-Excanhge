import context.CryptoOrderContext
import processors.CryptoOrderProcessor
import repository.IOrderRepository

class OrderService(private val repository: IOrderRepository) {
    private val processor = CryptoOrderProcessor()

    suspend fun exec(context: CryptoOrderContext) = processor.exec(context.applySettings())

    suspend fun createOrder(context: CryptoOrderContext) = processor.exec(context.applySettings())

    suspend fun readOrders(context: CryptoOrderContext) = processor.exec(context.applySettings())

    suspend fun deleteOrder(context: CryptoOrderContext) = processor.exec(context.applySettings())

    private fun CryptoOrderContext.applySettings() = apply {
        orderRepo = repository
    }
}
