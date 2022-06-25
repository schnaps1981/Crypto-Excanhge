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

suspend fun ApplicationCall.createOrder(orderService: OrderService) {

    val ctx = CryptoOrderContext()
    val request = receive<OrderCreateRequest>()
    ctx.fromTransport(request)
    orderService.createOrder(ctx)
    respond(ctx.toTransportCreateOrder())
}

suspend fun ApplicationCall.readOrders(orderService: OrderService) {

    val ctx = CryptoOrderContext()
    val request = receive<OrderReadRequest>()
    ctx.fromTransport(request)
    orderService.readOrders(ctx)
    respond(ctx.toTransportCreateOrder())
}

suspend fun ApplicationCall.deleteOrder(orderService: OrderService) {

    val ctx = CryptoOrderContext()
    val request = receive<OrderDeleteRequest>()
    ctx.fromTransport(request)
    orderService.deleteOrder(ctx)
    respond(ctx.toTransportCreateOrder())
}
