package workers.repo.order

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import context.CryptoOrderContext
import models.CryptoState
import repository.DbOrderRequest

fun ICorChainDsl<CryptoOrderContext>.repoOrderCreate(title: String) = worker {
    description = "Добавление ордера в БД"

    on { state == CryptoState.RUNNING }

    handle {
        val request = DbOrderRequest(orderRepoRead)

        val result = orderRepo.createOrder(request)
        val resultOrder = result.result

        if (result.isSuccess && resultOrder != null) {
            orderResponse = resultOrder
        } else {

            state = CryptoState.FAILED

            errors.addAll(result.errors)
        }
    }
}
