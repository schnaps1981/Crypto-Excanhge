package workers.stubs

import OrderStubs
import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import context.CryptoOrderContext
import models.CryptoError
import models.CryptoState
import stubs.CryptoOrderStubs

fun ICorChainDsl<CryptoOrderContext>.stubCreateOrderSuccess(title: String) = worker {
    this.title = title
    on { stubCase == CryptoOrderStubs.SUCCESS && state == CryptoState.RUNNING }
    handle {
        state = CryptoState.FINISHING

        orderResponse = OrderStubs.oneOrderStub
    }
}

fun ICorChainDsl<CryptoOrderContext>.stubCreateOrderFailed(title: String) = worker {
    this.title = title
    on { stubCase == CryptoOrderStubs.FAILED && state == CryptoState.RUNNING }
    handle {
        state = CryptoState.FAILED

        this.errors.add(
            CryptoError(
                code = "fail",
                group = "create",
                message = "can't create order from request with id ${requestId.asString()}"
            )
        )
    }
}

fun ICorChainDsl<CryptoOrderContext>.stubDeleteOrderSuccess(title: String) = worker {
    this.title = title
    on { stubCase == CryptoOrderStubs.SUCCESS && state == CryptoState.RUNNING }
    handle {
        state = CryptoState.FINISHING

        orderResponse = OrderStubs.deletedOrder.copy(orderId = orderRequest.orderId)
    }
}

fun ICorChainDsl<CryptoOrderContext>.stubDeleteOrderFailed(title: String) = worker {
    this.title = title
    on { stubCase == CryptoOrderStubs.FAILED && state == CryptoState.RUNNING }
    handle {
        state = CryptoState.FAILED

        this.errors.add(
            CryptoError(
                code = "fail",
                group = "delete",
                message = "cannot delete order with id = ${orderRequest.orderId.asString()}"
            )
        )
    }
}

fun ICorChainDsl<CryptoOrderContext>.stubDeleteOrderCannot(title: String) = worker {
    this.title = title
    on { stubCase == CryptoOrderStubs.CANNOT_DELETE && state == CryptoState.RUNNING }
    handle {
        state = CryptoState.FAILED
        this.errors.add(
            CryptoError(
                code = "cant",
                group = "delete",
                message = "can't delete order"
            )
        )
    }
}

fun ICorChainDsl<CryptoOrderContext>.stubValidationOrderBadId(title: String) = worker {
    this.title = title
    on { stubCase == CryptoOrderStubs.BAD_ID && state == CryptoState.RUNNING }
    handle {
        state = CryptoState.FAILED
        this.errors.add(
            CryptoError(
                group = "validation",
                field = this.orderRequest.orderId::class.java.simpleName,
                message = "wrong id"
            )
        )
    }
}

fun ICorChainDsl<CryptoOrderContext>.stubReadOrderSuccess(title: String) = worker {
    this.title = title
    on { stubCase == CryptoOrderStubs.SUCCESS && state == CryptoState.RUNNING }
    handle {
        state = CryptoState.FINISHING

        ordersResponse = OrderStubs.listOfOrders
    }
}

fun ICorChainDsl<CryptoOrderContext>.stubReadOrderFailed(title: String) = worker {
    this.title = title
    on { stubCase == CryptoOrderStubs.FAILED && state == CryptoState.RUNNING }
    handle {
        state = CryptoState.FAILED

        this.errors.add(
            CryptoError(
                code = "fail",
                group = "read",
                message = "can't read order from request with id ${requestId.asString()}"
            )
        )
    }
}

fun ICorChainDsl<CryptoOrderContext>.stubReadOrderNotFound(title: String) = worker {
    this.title = title
    on { stubCase == CryptoOrderStubs.NOT_FOUND && state == CryptoState.RUNNING }
    handle {
        state = CryptoState.FAILED

        this.errors.add(
            CryptoError(
                code = "notFound",
                group = "read",
                message = "orders not found"
            )
        )
    }
}

fun ICorChainDsl<CryptoOrderContext>.stubValidationOrderBadFilter(title: String) = worker {
    this.title = title
    on { stubCase == CryptoOrderStubs.BAD_FILTER && state == CryptoState.RUNNING }
    handle {
        state = CryptoState.FAILED

        this.errors.add(
            CryptoError(
                code = "badFilter",
                group = "validation",
                message = "request can't be executed. Filter is bad"
            )
        )
    }
}
