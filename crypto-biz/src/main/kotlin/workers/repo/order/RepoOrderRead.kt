package workers.repo.order

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import context.CryptoOrderContext
import models.CryptoState
import repository.DbOrderIdRequest

fun ICorChainDsl<CryptoOrderContext>.repoOrderRead(title: String) = worker {
    this.title = title
    description = "Чтение ордера из БД."
    on { state == CryptoState.RUNNING }
    handle {
        val request = DbOrderIdRequest(orderValidated.orderId)
        val result = orderRepo.readOrder(request)
        val resultOrder = result.result
        if (result.isSuccess && resultOrder != null) {
            orderRepoPrepare = resultOrder
        } else {
            state = CryptoState.FAILED
            errors.addAll(result.errors)
        }
    }
}
