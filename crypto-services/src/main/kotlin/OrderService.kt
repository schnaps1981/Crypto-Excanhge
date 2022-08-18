import context.CryptoOrderContext
import models.CryptoSettings
import processors.CryptoOrderProcessor

class OrderService(settings: CryptoSettings) {
    private val processor = CryptoOrderProcessor(settings)

    suspend fun exec(context: CryptoOrderContext) = processor.exec(context)

    suspend fun createOrder(context: CryptoOrderContext) = processor.exec(context)

    suspend fun readOrders(context: CryptoOrderContext) = processor.exec(context)

    suspend fun deleteOrder(context: CryptoOrderContext) = processor.exec(context)
}
