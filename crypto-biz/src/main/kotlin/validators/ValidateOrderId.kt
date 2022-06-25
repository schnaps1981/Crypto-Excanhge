package validators

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import context.CryptoOrderContext
import context.fail
import helpers.errorValidation

fun ICorChainDsl<CryptoOrderContext>.validateOrderId(title: String) = worker {
    this.title = title

    on { orderValidating.orderId.asString().isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "orderId",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}
