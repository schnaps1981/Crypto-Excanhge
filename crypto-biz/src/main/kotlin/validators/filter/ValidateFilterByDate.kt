package validators.filter

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import context.CryptoOrderContext
import context.fail
import helpers.errorValidation
import models.filter.CryptoFilterByDate

fun ICorChainDsl<CryptoOrderContext>.validateFilterDate(title: String) = worker {
    this.title = title

    on { (orderFilterValidating as? CryptoFilterByDate)?.orderDate?.isEmpty() == true }
    handle {
        fail(
            errorValidation(
                field = "filter",
                violationCode = "date",
                description = "field must not be empty"
            )
        )
    }
}