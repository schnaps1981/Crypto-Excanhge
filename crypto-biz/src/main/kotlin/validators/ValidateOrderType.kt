package validators

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import context.CryptoOrderContext
import context.fail
import helpers.errorValidation
import models.CryptoOrderType

fun ICorChainDsl<CryptoOrderContext>.validateOrderType(title: String) = worker {
    this.title = title

    on { orderValidating.orderType == CryptoOrderType.NONE }
    handle {
        fail(
            errorValidation(
                field = "orderType",
                violationCode = "none",
                description = "field must not be NONE"
            )
        )
    }
}
