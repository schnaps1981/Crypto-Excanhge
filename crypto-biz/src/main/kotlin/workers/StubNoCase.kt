package workers

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import context.CryptoBaseContext
import models.CryptoError
import models.CryptoState

fun <CTX : CryptoBaseContext<*, *>> ICorChainDsl<CTX>.stubNoCase(title: String) = worker {
    this.title = title
    on { state == CryptoState.RUNNING }
    handle {
        state = CryptoState.FAILED
        this.errors.add(
            CryptoError(
                code = "validation",
                field = "stub",
                group = "validation",
                message = "Wrong stub case is requested: ${stubCase!!}"
            )
        )
    }
}
