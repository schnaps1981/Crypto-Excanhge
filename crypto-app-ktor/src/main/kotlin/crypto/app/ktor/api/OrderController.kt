package crypto.app.ktor.api

import OrderService
import com.crypto.api.v1.models.OrderCreateRequest
import com.crypto.api.v1.models.OrderDeleteRequest
import com.crypto.api.v1.models.OrderReadRequest
import context.CryptoOrderContext
import fromTransport
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import toTransportCreateOrder
import toTransportDeleteOrder
import toTransportReadOrder

suspend fun ApplicationCall.createOrder(orderService: OrderService) {
    val request = receive<OrderCreateRequest>()

    respond(
        CryptoOrderContext().apply { fromTransport(request) }.let {
            orderService.createOrder(it)
        }.toTransportCreateOrder()
    )
}

suspend fun ApplicationCall.readOrders(orderService: OrderService) {
    val request = receive<OrderReadRequest>()

    respond(
        CryptoOrderContext().apply { fromTransport(request) }.let {
            orderService.readOrders(it)
        }.toTransportReadOrder()
    )
}

suspend fun ApplicationCall.deleteOrder(orderService: OrderService) {
    val request = receive<OrderDeleteRequest>()

    respond(
        CryptoOrderContext().apply { fromTransport(request) }.let {
            orderService.deleteOrder(it)
        }.toTransportDeleteOrder()
    )
}
