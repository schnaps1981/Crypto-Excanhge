package validators

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import context.CryptoOrderContext
import context.fail
import helpers.errorValidation

fun ICorChainDsl<CryptoOrderContext>.validateTradePair(title: String) = worker {
    this.title = title

    on { orderValidating.pair.first.isEmpty() || orderValidating.pair.second.isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "pair",
                violationCode = "empty",
                description = "field must not be empty or partial filled"
            )
        )
    }
}
