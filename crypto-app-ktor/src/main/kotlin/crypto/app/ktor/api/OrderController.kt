package crypto.app.ktor.api

import OrderService
import com.crypto.api.v1.models.*
import crypto.app.ktor.helpers.orderControllerHelper
import io.ktor.server.application.*
import models.commands.CryptoOrderCommands
import ru.otus.otuskotlin.marketplace.logging.LogWrapper

suspend fun ApplicationCall.createOrder(orderService: OrderService, logger: LogWrapper) {

    orderControllerHelper<OrderCreateRequest, OrderCreateResponse>(
        logger = logger,
        logId = "ad-create",
        command = CryptoOrderCommands.CREATE
    ) {
        orderService.createOrder(this)
    }
}

suspend fun ApplicationCall.readOrders(orderService: OrderService, logger: LogWrapper) {

    orderControllerHelper<OrderReadRequest, OrderReadResponse>(
        logger = logger,
        logId = "ad-create",
        command = CryptoOrderCommands.READ
    ) {
        orderService.readOrders(this)
    }
}

suspend fun ApplicationCall.deleteOrder(orderService: OrderService, logger: LogWrapper) {

    orderControllerHelper<OrderDeleteRequest, OrderDeleteResponse>(
        logger = logger,
        logId = "ad-create",
        command = CryptoOrderCommands.DELETE
    ) {
        orderService.deleteOrder(this)
    }
}
