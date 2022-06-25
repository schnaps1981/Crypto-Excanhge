import context.CryptoOrderContext
import processors.CryptoOrderProcessor

class OrderService {

    private val processor = CryptoOrderProcessor()

    suspend fun createOrder(context: CryptoOrderContext) = processor.exec(context)

    suspend fun readOrders(context: CryptoOrderContext) = processor.exec(context)

    suspend fun deleteOrder(context: CryptoOrderContext) = processor.exec(context)

}
