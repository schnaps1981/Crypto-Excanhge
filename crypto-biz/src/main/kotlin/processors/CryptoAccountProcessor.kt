package processors

import com.crowdproj.kotlin.cor.rootChain
import context.CryptoAccountContext
import workers.initStatus

class CryptoAccountProcessor {

    suspend fun exec(context: CryptoAccountContext) = AccountChain.exec(context)

    companion object {
        private val AccountChain = rootChain<CryptoAccountContext> {
            initStatus("Инициализация статуса")
        }.build()
    }
}
