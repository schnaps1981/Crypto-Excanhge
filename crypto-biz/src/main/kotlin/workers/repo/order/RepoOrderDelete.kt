package workers.repo.order

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import context.CryptoOrderContext
import models.CryptoState
import repository.DbOrderIdRequest

fun ICorChainDsl<CryptoOrderContext>.repoOrderDelete(title: String) = worker {
    description = "Удаление объявления из БД по ID"

    on { state == CryptoState.RUNNING }

    handle {
        val request = DbOrderIdRequest(orderRepoPrepare.orderId, orderRepoPrepare.lock)
        val result = orderRepo.deleteOrder(request)
        val resultOrder = result.result

        if (result.isSuccess && resultOrder != null) {
            orderResponse = resultOrder
        } else {
            state = CryptoState.FAILED

            errors.addAll(result.errors)
        }
    }
}
