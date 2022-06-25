package validators.filter

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import context.CryptoOrderContext
import context.fail
import helpers.errorValidation
import models.CryptoOrderState
import models.filter.CryptoFilterByState

fun ICorChainDsl<CryptoOrderContext>.validateFilterState(title: String) = worker {
    this.title = title

    on { (orderFilterValidating as? CryptoFilterByState)?.orderState == CryptoOrderState.NONE }
    handle {
        fail(
            errorValidation(
                field = "filter",
                violationCode = "state",
                description = "field must not be NONE"
            )
        )
    }
}