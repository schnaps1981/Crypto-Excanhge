package workers.repo.order

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import context.CryptoOrderContext
import context.fail
import helpers.errorConcurrency
import models.CryptoState

fun ICorChainDsl<CryptoOrderContext>.repoCheckReadLock(title: String) = worker {

    description = "Проверка совпадения блокировки с блокировкой в БД"

    on { state == CryptoState.RUNNING && orderValidated.lock != orderRepoRead.lock }

    handle {
        fail(errorConcurrency(violationCode = "changed", "Object has been inconsistently changed"))

        orderRepoDone = orderRepoRead
    }
}
