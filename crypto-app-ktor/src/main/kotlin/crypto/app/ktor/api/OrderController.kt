package crypto.app.ktor.api

import OrderService
import com.crypto.api.v1.models.*
import crypto.app.ktor.helpers.orderControllerHelper
import io.ktor.server.application.*
import models.commands.CryptoOrderCommands

suspend fun ApplicationCall.createOrder(orderService: OrderService) {

    orderControllerHelper<OrderCreateRequest, OrderCreateResponse>(CryptoOrderCommands.CREATE) {
        orderService.createOrder(this)
    }
}

suspend fun ApplicationCall.readOrders(orderService: OrderService) {

    orderControllerHelper<OrderReadRequest, OrderReadResponse>(CryptoOrderCommands.READ) {
        orderService.readOrders(this)
    }
}

suspend fun ApplicationCall.deleteOrder(orderService: OrderService) {

    orderControllerHelper<OrderDeleteRequest, OrderDeleteResponse>(CryptoOrderCommands.DELETE) {
        orderService.deleteOrder(this)
    }
}
