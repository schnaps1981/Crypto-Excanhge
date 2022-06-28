package validators

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import context.CryptoOrderContext
import context.fail
import helpers.errorValidation
import models.CryptoOrderId

fun ICorChainDsl<CryptoOrderContext>.validateOrderId(title: String) = worker {
    this.title = title

    on { orderValidating.orderId == CryptoOrderId.NONE }
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
