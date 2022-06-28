package validators

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import context.CryptoOrderContext
import context.fail
import helpers.errorValidation
import models.CryptoOrder

fun ICorChainDsl<CryptoOrderContext>.validateAmount(title: String) = worker {
    this.title = title

    on { orderValidating.amount == CryptoOrder.ZERO }
    handle {
        fail(
            errorValidation(
                field = "amount",
                violationCode = "zero",
                description = "field must not be zero"
            )
        )
    }
}
