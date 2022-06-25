package groups

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.chain
import context.CryptoBaseContext
import models.CryptoState
import models.commands.CryptoOrderCommands

fun <CTX : CryptoBaseContext<*, *>, CMD : CryptoOrderCommands> ICorChainDsl<CTX>.operation(
    title: String,
    command: CMD,
    block: ICorChainDsl<CTX>.() -> Unit
) = chain {
    block()
    this.title = title
    on { this.command == command && state == CryptoState.RUNNING }
}
