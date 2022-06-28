package validators

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import context.CryptoOrderContext
import context.fail
import helpers.errorValidation
import models.CryptoOrder

fun ICorChainDsl<CryptoOrderContext>.validatePrice(title: String) = worker {
    this.title = title

    on { orderValidating.price == CryptoOrder.ZERO }
    handle {
        fail(
            errorValidation(
                field = "price",
                violationCode = "zero",
                description = "field must not be zero"
            )
        )
    }
}
