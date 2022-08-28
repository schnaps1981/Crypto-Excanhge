package processors

import com.crowdproj.kotlin.cor.handlers.chain
import com.crowdproj.kotlin.cor.handlers.worker
import com.crowdproj.kotlin.cor.rootChain
import context.CryptoOrderContext
import groups.operation
import groups.stubs
import models.CryptoLock
import models.CryptoOrderId
import models.CryptoSettings
import models.commands.CryptoOrderCommands
import validators.*
import validators.filter.validateFilterCurrency
import validators.filter.validateFilterDate
import validators.filter.validateFilterState
import validators.filter.validateFilterType
import workers.finishProcess
import workers.initRepo
import workers.initStatus
import workers.repo.order.*
import workers.stubNoCase
import workers.stubs.*


class CryptoOrderProcessor(private val settings: CryptoSettings) {

    suspend fun exec(context: CryptoOrderContext) =
        OrderChain.exec(context.apply { settings = this@CryptoOrderProcessor.settings })

    companion object {
        private val OrderChain = rootChain<CryptoOrderContext> {
            initStatus("Инициализация статуса")
            initRepo("Инициализация репозитория")

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
                    validatePrice("Валидация цены актива")
                    validateOrderType("Валидация типа ордера")

                    finishOrderValidation("Успешное завершение валидации запроса") {
                        orderValidated = orderValidating
                    }
                }

                repoOrderCreate("Создание ордера в БД")

                finishProcess("завершение процесса обработки запроса")
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
                    worker("Очистка id") {
                        orderValidating.orderId = CryptoOrderId(orderValidating.orderId.asString().trim())
                    }
                    worker("Очистка lock") { orderValidating.lock = CryptoLock(orderValidating.lock.asString().trim()) }

                    validateOrderId("Валидация id ордера")
                    validateLockNotEmpty("Проверка на непустой lock")

                    finishOrderValidation("Успешное завершение валидации запроса") {
                        orderValidated = orderValidating
                    }
                }

                repoOrderRead("Чтение ордера из БД. Получаем блокировку")
                repoCheckReadLock("Проверяем блокировку")
                repoPrepareDelete("Подготовка объекта для удаления")
                repoOrderDelete("Удаление ордера из БД")

                finishProcess("завершение процесса обработки запроса")
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

                repoOrdersRead("Чтение ордеров из БД")

                finishProcess("завершение процесса обработки запроса")
            }

        }.build()
    }
}
