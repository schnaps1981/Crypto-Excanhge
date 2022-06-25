package validators

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import context.CryptoOrderContext
import context.fail
import helpers.errorValidation

fun ICorChainDsl<CryptoOrderContext>.validateQuantity(title: String) = worker {
    this.title = title

    on { orderValidating.quantity == 0.0.toBigDecimal() }
    handle {
        fail(
            errorValidation(
                field = "quantity",
                violationCode = "zero",
                description = "field must not be zero"
            )
        )
    }
}
