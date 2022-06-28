package validators

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import context.CryptoBaseContext
import models.CryptoState

fun <CTX : CryptoBaseContext<*, *>> ICorChainDsl<CTX>.finishOrderValidation(title: String, block: CTX.() -> Unit) =
    worker {
        this.title = title
        on { state == CryptoState.RUNNING }
        handle {
            block()
        }
    }
