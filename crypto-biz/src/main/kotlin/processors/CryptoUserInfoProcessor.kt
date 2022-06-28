package processors

import com.crowdproj.kotlin.cor.rootChain
import context.CryptoUserInfoContext
import workers.initStatus

class CryptoUserInfoProcessor {

    suspend fun exec(context: CryptoUserInfoContext) = UserChain.exec(context)

    companion object {
        private val UserChain = rootChain<CryptoUserInfoContext> {
            initStatus("Инициализация статуса")
        }.build()
    }
}
