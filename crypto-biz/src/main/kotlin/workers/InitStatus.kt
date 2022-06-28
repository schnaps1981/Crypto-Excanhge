package workers

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import context.CryptoBaseContext
import models.CryptoState

fun <CTX : CryptoBaseContext<*, *>> ICorChainDsl<CTX>.initStatus(title: String) = worker {
    this.title = title
    on { state == CryptoState.NONE }
    handle { state = CryptoState.RUNNING }
}
