package validators.filter

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import context.CryptoOrderContext
import context.fail
import helpers.errorValidation
import models.filter.CryptoFilterByCurrency

fun ICorChainDsl<CryptoOrderContext>.validateFilterCurrency(title: String) = worker {
    this.title = title

    on { (orderFilterValidating as? CryptoFilterByCurrency)?.ticker?.isEmpty() == true }
    handle {
        fail(
            errorValidation(
                field = "filter",
                violationCode = "ticker",
                description = "field must not be empty"
            )
        )
    }
}