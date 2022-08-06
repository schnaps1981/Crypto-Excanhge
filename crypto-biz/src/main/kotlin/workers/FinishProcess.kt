package workers

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import context.CryptoBaseContext
import models.CryptoState
import models.CryptoWorkMode

fun <CTX : CryptoBaseContext<*, *>> ICorChainDsl<CTX>.finishProcess(title: String) = worker {
    this.title = title
    on { workMode != CryptoWorkMode.STUB }
    handle {
        state = when (state) {
            CryptoState.RUNNING -> CryptoState.FINISHING
            else -> state
        }
    }
}
