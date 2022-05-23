package crypto.app.ktor.api.service

import OrderStubs
import context.CryptoOrderContext
import crypto.app.ktor.api.errorResponse
import crypto.app.ktor.api.successResponse
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
                (this as CryptoOrderContext).orderResponse = resp
            }

            else -> context.errorResponse {
                it.copy(field = "", message = "error create order with request: ${context.orderRequest}")
            }
        } as CryptoOrderContext
    }

    fun readOrders(context: CryptoOrderContext): CryptoOrderContext {

        val resp = when (context.workMode) {
            CryptoWorkMode.PROD -> TODO("prod not implemented yet")
            CryptoWorkMode.TEST -> mutableListOf(context.orderRequest)
            CryptoWorkMode.STUB -> OrderStubs.listOfOrders
        }

        return when (context.stubCase) {
            CryptoOrderStubs.SUCCESS, CryptoOrderStubs.NONE -> context.successResponse {
                (this as CryptoOrderContext).ordersResponse = resp
            }

            else -> context.errorResponse {
                it.copy(field = "", message = "error read order with request: ${context.orderRequest}")
            }
        } as CryptoOrderContext
    }

    fun deleteOrder(context: CryptoOrderContext): CryptoOrderContext {
        val resp = when (context.workMode) {
            CryptoWorkMode.PROD -> TODO("prod not implemented yet")
            CryptoWorkMode.TEST -> context.orderRequest
            CryptoWorkMode.STUB -> OrderStubs.deletedOrder
        }

        return when (context.stubCase) {
            CryptoOrderStubs.SUCCESS, CryptoOrderStubs.NONE -> context.successResponse {
                (this as CryptoOrderContext).orderResponse = resp
            }

            else -> context.errorResponse {
                it.copy(field = "", message = "error delete order with request: ${context.orderRequest}")
            }
        } as CryptoOrderContext
    }
}
