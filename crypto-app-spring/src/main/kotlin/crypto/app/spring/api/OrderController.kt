package crypto.app.spring.api

import OrderService
import com.crypto.api.v1.models.*
import context.CryptoOrderContext
import fromTransport
import kotlinx.coroutines.runBlocking
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
    fun createOrder(@RequestBody createOrderRequest: OrderCreateRequest): OrderCreateResponse = runBlocking {
        CryptoOrderContext().apply {
            fromTransport(createOrderRequest)
            orderService.createOrder(this)
        }.toTransportCreateOrder()
    }

    @PostMapping("read")
    fun readOrders(@RequestBody readOrderRequest: OrderReadRequest): OrderReadResponse = runBlocking {
        CryptoOrderContext().apply {
            fromTransport(readOrderRequest)
            orderService.readOrders(this)
        }.toTransportReadOrder()
    }

    @PostMapping("delete")
    fun deleteOrder(@RequestBody deleteOrderRequest: OrderDeleteRequest): OrderDeleteResponse = runBlocking {
        CryptoOrderContext().apply {
            fromTransport(deleteOrderRequest)
            orderService.deleteOrder(this)
        }.toTransportDeleteOrder()
    }
}
