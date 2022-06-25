package validators.filter

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import context.CryptoOrderContext
import context.fail
import helpers.errorValidation
import models.CryptoOrderType
import models.filter.CryptoFilterByType

fun ICorChainDsl<CryptoOrderContext>.validateFilterType(title: String) = worker {
    this.title = title

    on { (orderFilterValidating as? CryptoFilterByType)?.orderType == CryptoOrderType.NONE }
    handle {
        fail(
            errorValidation(
                field = "filter",
                violationCode = "type",
                description = "field must not be NONE"
            )
        )
    }
}