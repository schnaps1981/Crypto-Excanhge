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
        val resultAd = result.result
        if (result.isSuccess && resultAd != null) {
            orderRepoPrepare = resultAd
        } else {
            state = CryptoState.FAILED
            errors.addAll(result.errors)
        }
    }
}
