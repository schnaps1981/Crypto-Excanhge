package workers.repo.order

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import context.CryptoOrderContext
import models.CryptoState
import repository.DbOrderFilterRequest

fun ICorChainDsl<CryptoOrderContext>.repoOrdersRead(title: String) = worker {

    on { state == CryptoState.RUNNING }

    handle {
        val request = DbOrderFilterRequest(
            ownerId = principal.id,
            filter = orderFilterValidated
        )

        val result = orderRepo.searchOrders(request)
        val resultOrders = result.result

        if (result.isSuccess && resultOrders != null) {
            ordersRepoRead = resultOrders.toMutableList()
        } else {
            state = CryptoState.FAILED

            errors.addAll(result.errors)
        }
    }
}
