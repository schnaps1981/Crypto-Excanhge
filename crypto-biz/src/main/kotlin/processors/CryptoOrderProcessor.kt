package processors

import com.crowdproj.kotlin.cor.handlers.chain
import com.crowdproj.kotlin.cor.handlers.worker
import com.crowdproj.kotlin.cor.rootChain
import context.CryptoOrderContext
import groups.operation
import groups.stubs
import models.commands.CryptoOrderCommands
import validators.*
import validators.filter.validateFilterCurrency
import validators.filter.validateFilterDate
import validators.filter.validateFilterState
import validators.filter.validateFilterType
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

                chain {
                    title = "Валидация запроса создания ордера"
                    worker("копирование полей в Validating") { orderValidating = orderRequest.deepCopy() }

                    validateTradePair("Проверка торговой пары")
                    validateQuantity("Валидация количества актива")
                    //validateAmount("Валидация суммы актива")
                    validatePrice("Валидация цены актива")
                    validateOrderType("Валидация типа ордера")

                    finishOrderValidation("Успешное завершение валидации запроса") {
                        orderValidated = orderValidating
                    }
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

                chain {
                    title = "Валидация запроса удаления ордера"
                    worker("копирование полей в Validating") { orderValidating = orderRequest.deepCopy() }

                    validateOrderId("Валидация id ордера")

                    finishOrderValidation("Успешное завершение валидации запроса") {
                        orderValidated = orderValidating
                    }
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

                chain {
                    title = "Валидация запроса чтения ордера"
                    worker("копирование полей в Validating") { orderFilterValidating = orderFilter.deepCopy() }

                    validateFilterCurrency("Проверка фильра по валюте ордера")
                    validateFilterDate("Проверка фильтра по дате ордера")
                    validateFilterState("Проверка фильтра по состоянию ордера")
                    validateFilterType("Проверка фильтра по типу ордера")

                    finishOrderValidation("Успешное завершение валидации фильтра") {
                        orderFilterValidated = orderFilterValidating
                    }
                }
            }

        }.build()
    }
}
