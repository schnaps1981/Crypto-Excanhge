package groups

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.chain
import context.CryptoBaseContext
import models.CryptoState
import models.CryptoWorkMode

fun <CTX> ICorChainDsl<CTX>.stubs(
    title: String,
    block: ICorChainDsl<CTX>.() -> Unit
) where CTX : CryptoBaseContext<*, *> = chain {
    block()
    this.title = title
    on { workMode == CryptoWorkMode.STUB && state == CryptoState.RUNNING }
}
