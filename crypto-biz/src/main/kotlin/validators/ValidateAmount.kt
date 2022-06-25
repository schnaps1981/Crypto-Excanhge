package validators

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import context.CryptoOrderContext
import context.fail
import helpers.errorValidation

fun ICorChainDsl<CryptoOrderContext>.validateAmount(title: String) = worker {
    this.title = title

    on { orderValidating.amount == 0.0.toBigDecimal() }
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
