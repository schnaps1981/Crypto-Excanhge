package processors

import com.crowdproj.kotlin.cor.rootChain
import context.CryptoTickerContext
import workers.initStatus

class CryptoTickerProcessor {

    suspend fun exec(context: CryptoTickerContext) = TickerChain.exec(context)

    companion object {
        private val TickerChain = rootChain<CryptoTickerContext> {
            initStatus("Инициализация статуса")
        }.build()
    }
}
