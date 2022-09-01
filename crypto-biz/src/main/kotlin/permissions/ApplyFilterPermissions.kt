package permissions

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.chain
import com.crowdproj.kotlin.cor.handlers.worker
import context.CryptoOrderContext
import models.CryptoFilterApplyTo
import models.CryptoState
import models.CryptoUserPermissions

fun ICorChainDsl<CryptoOrderContext>.applyFilerPermissions(title: String) = chain {
    this.title = title
    description = "Добавление ограничений в запрос чтения согласно правам доступа и др. политикам"
    on { state == CryptoState.RUNNING }
    worker("Определение типа фильтрации") {
        orderFilterValidated.filterPermissions = setOfNotNull(
            CryptoFilterApplyTo.OWN.takeIf { chainPermissions.contains(CryptoUserPermissions.FILTER_OWN) },
            CryptoFilterApplyTo.ANY.takeIf { chainPermissions.contains(CryptoUserPermissions.FILTER_ANY) },
        ).toMutableSet()
    }
}
