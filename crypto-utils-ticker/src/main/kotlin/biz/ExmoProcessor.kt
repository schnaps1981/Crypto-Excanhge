package biz

import biz.workers.initRepo
import biz.workers.initStatus
import com.crowdproj.kotlin.cor.handlers.worker
import com.crowdproj.kotlin.cor.rootChain
import common.ExmoContext
import common.models.ExmoState
import models.CryptoOrderState
import models.CryptoOrderType
import models.CryptoSettings
import models.CryptoUserId
import models.filter.ICryptoFilter
import repository.DbOrderFilterRequest
import repository.DbOrderRequest

class ExmoProcessor(private val settings: CryptoSettings) {

    suspend fun exec(context: ExmoContext) = ExmoChain.exec(context.apply { settings = this@ExmoProcessor.settings })

    companion object {
        private val ExmoChain = rootChain<ExmoContext> {
            initStatus("Инициализация статуса логики исполнения ордеров")
            initRepo("Инициализация репозитория")


            worker {
                description = "Прочитать активные ордера по определенной торговой паре"
                on { state == ExmoState.RUNNING }
                handle {
                    val request = DbOrderFilterRequest(
                        ownerId = CryptoUserId.NONE,
                        filter = ICryptoFilter.NONE
                    )

                    val result = exmoRepo.searchOrders(request)

                    val resultOrders = result.result?.filter { it.orderState == CryptoOrderState.ACTIVE } ?: emptyList()

                    if (result.isSuccess && resultOrders.isNotEmpty()) {
                        val updatedOrders = resultOrders.mapNotNull {
                            if (
                                (it.orderType == CryptoOrderType.SELL && exmoInData.data.lastTrade >= it.price) ||
                                (it.orderType == CryptoOrderType.BUY && exmoInData.data.lastTrade <= it.price)
                            ) {
                                it.copy(orderState = CryptoOrderState.COMPLETED, amount = it.price * it.quantity)
                            } else {
                                null
                            }
                        }

                        updatedOrders.forEach {
                            exmoRepo.updateOrder(DbOrderRequest(it))
                        }
                    } else {
                        state = ExmoState.FAILED
                    }
                }

            }
        }.build()
    }
}