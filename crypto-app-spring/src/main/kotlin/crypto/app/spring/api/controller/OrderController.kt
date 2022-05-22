package crypto.app.spring.api.controller

import com.crypto.api.v1.models.*
import context.CryptoOrderContext
import crypto.app.spring.api.service.OrderService
import fromTransport
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import toTransportCreateOrder
import toTransportDeleteOrder
import toTransportReadOrder

@RestController
@RequestMapping("order")
class OrderController(
    private val orderService: OrderService
) {

    @PostMapping("create")
    fun createOrder(@RequestBody createOrderRequest: OrderCreateRequest): OrderCreateResponse {
        val context = CryptoOrderContext().apply {
            fromTransport(createOrderRequest)
        }

        return orderService.createOrder(context).toTransportCreateOrder()
    }

    @PostMapping("read")
    fun readOrders(@RequestBody readOrderRequest: OrderReadRequest): OrderReadResponse {
        val context = CryptoOrderContext().apply {
            fromTransport(readOrderRequest)
        }

        return orderService.readOrders(context).toTransportReadOrder()
    }

    @PostMapping("delete")
    fun deleteOrder(@RequestBody deleteOrderRequest: OrderDeleteRequest): OrderDeleteResponse {
        val context = CryptoOrderContext().apply {
            fromTransport(deleteOrderRequest)
        }

        return orderService.deleteOrder(context).toTransportDeleteOrder()
    }
}
