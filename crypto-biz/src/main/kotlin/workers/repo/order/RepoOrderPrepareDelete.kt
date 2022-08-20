package workers.repo.order

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import context.CryptoOrderContext
import models.CryptoState

fun ICorChainDsl<CryptoOrderContext>.repoPrepareDelete(title: String) = worker {

    description = "Готовим данные к удалению из БД"

    on { state == CryptoState.RUNNING }

    handle {
        orderRepoPrepare = orderValidated.deepCopy()
    }
}
