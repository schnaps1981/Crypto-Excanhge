package processors

import com.crowdproj.kotlin.cor.rootChain
import context.CryptoOrderContext
import groups.operation
import groups.stubs
import models.commands.CryptoOrderCommands
import workers.initStatus
import workers.stubNoCase
import workers.stubs.*


class CryptoOrderProcessor {

    suspend fun exec(context: CryptoOrderContext) = OrderChain.exec(context)

    companion object {
        private val OrderChain = rootChain<CryptoOrderContext> {
            initStatus("Инициализация статуса")

            operation("Создание ордера", CryptoOrderCommands.CREATE) {
                stubs("Обработка стабов") {
                    stubCreateOrderSuccess("Имитация успешного создания ордера")
                    stubCreateOrderFailed("Имитация неудачного создания ордера")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
            }

            operation("Удаление ордера", CryptoOrderCommands.DELETE) {
                stubs("Обработка стабов") {
                    stubDeleteOrderSuccess("Имитация успешного удаления")
                    stubDeleteOrderFailed("Имитация неудачного удаления")
                    stubDeleteOrderCannot("Имитация невозможности удаления")
                    stubValidationOrderBadId("Имитация неверного ID")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
            }

            operation("Чтение ордера", CryptoOrderCommands.READ) {
                stubs("Обработка стабов") {
                    stubReadOrderSuccess("Имитация успешного чтения списка ордеров")
                    stubReadOrderFailed("Имитация ошибки при чтении ордеров")
                    stubReadOrderNotFound("Имитация возврата пустого списка (Ордер не найден)")
                    stubValidationOrderBadFilter("Имитация неверного фильтра")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
            }

        }.build()
    }
}
