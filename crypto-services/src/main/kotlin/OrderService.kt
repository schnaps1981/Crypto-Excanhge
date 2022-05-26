import context.CryptoOrderContext
import models.CryptoWorkMode
import stubs.CryptoOrderStubs

class OrderService {

    fun createOrder(context: CryptoOrderContext): CryptoOrderContext {

        val resp = when (context.workMode) {
            CryptoWorkMode.PROD -> TODO("prod not implemented yet")
            CryptoWorkMode.TEST -> context.orderRequest //в тесте отправляем назад то, что получили
            CryptoWorkMode.STUB -> OrderStubs.oneOrderStub//в стабе отправляем заранее заготовленную заглушку
        }

        return when (context.stubCase) {
            CryptoOrderStubs.SUCCESS, CryptoOrderStubs.NONE -> context.successResponse {
                orderResponse = resp
            }

            else -> context.errorResponse {
                it.copy(field = "", message = "error create order with request: ${context.orderRequest}")
            }
        }
    }

    fun readOrders(context: CryptoOrderContext): CryptoOrderContext {

        val resp = when (context.workMode) {
            CryptoWorkMode.PROD -> TODO("prod not implemented yet")
            CryptoWorkMode.TEST -> mutableListOf(context.orderRequest)
            CryptoWorkMode.STUB -> OrderStubs.listOfOrders
        }

        return when (context.stubCase) {
            CryptoOrderStubs.SUCCESS, CryptoOrderStubs.NONE -> context.successResponse {
                ordersResponse = resp
            }

            else -> context.errorResponse {
                it.copy(field = "", message = "error read order with request: ${context.orderRequest}")
            }
        }
    }

    fun deleteOrder(context: CryptoOrderContext): CryptoOrderContext {
        val resp = when (context.workMode) {
            CryptoWorkMode.PROD -> TODO("prod not implemented yet")
            CryptoWorkMode.TEST -> context.orderRequest
            CryptoWorkMode.STUB -> OrderStubs.deletedOrder
        }

        return when (context.stubCase) {
            CryptoOrderStubs.SUCCESS, CryptoOrderStubs.NONE -> context.successResponse {
                orderResponse = resp
            }

            else -> context.errorResponse {
                it.copy(field = "", message = "error delete order with request: ${context.orderRequest}")
            }
        }
    }
}
