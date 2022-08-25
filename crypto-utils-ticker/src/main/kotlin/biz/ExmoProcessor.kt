package biz

import biz.workers.initRepo
import biz.workers.initStatus
import com.crowdproj.kotlin.cor.handlers.chain
import com.crowdproj.kotlin.cor.handlers.worker
import com.crowdproj.kotlin.cor.rootChain
import common.ExmoContext
import models.CryptoSettings

class ExmoProcessor(private val settings: CryptoSettings, tickers: List<Pair<String, String>>) {

    suspend fun exec(context: ExmoContext) = ExmoChain.exec(context.apply { settings = this@ExmoProcessor.settings })

    companion object {
        private val ExmoChain = rootChain<ExmoContext> {
            initStatus("Инициализация статуса логики исполнения ордеров")
            initRepo("Инициализация репозитория")

            //Прочитать активные ордера по определенной торговой паре

            //в отдельной цепочке преобразовать их соответственно текущего курса, цены ордера

            //обновить в БД

            chain {
                worker(title = "Проверка состояния ордеров, при изменении тикера") {
                    println("WS TOPIC IN CHAIN! ${exmoInData.topic}")
                }
            }

        }.build()
    }
}