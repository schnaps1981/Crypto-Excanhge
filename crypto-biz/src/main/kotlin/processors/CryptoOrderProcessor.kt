package processors

import com.crowdproj.kotlin.cor.handlers.chain
import com.crowdproj.kotlin.cor.handlers.worker
import com.crowdproj.kotlin.cor.rootChain
import context.CryptoOrderContext
import context.fail
import groups.operation
import groups.stubs
import models.*
import models.commands.CryptoOrderCommands
import permissions.*
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
                        orderValidated = orderValidating.deepCopy()
                    }
                }

                chainPermissions("Вычисление разрешений для пользователя")
                worker {
                    title = "инициализация orderRead"
                    on { state == CryptoState.RUNNING }
                    handle {
                        orderRepoRead = orderValidated.deepCopy()
                        orderRepoRead.ownerId = principal.id
                    }
                }
                accessValidation("Вычисление прав доступа")

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
                        orderValidated = orderValidating.deepCopy()
                    }
                }

                chainPermissions("Вычисление разрешений для пользователя")

                repoOrderRead("Чтение ордера из БД. Получаем блокировку")

                accessValidation("Вычисление прав доступа")

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
                        orderFilterValidated = orderFilterValidating.deepCopy()
                    }
                }

                chainPermissions("Вычисление разрешений для пользователя")
                applyFilerPermissions("Настройка разрешений для фильтра")

                repoOrdersRead("Чтение ордеров из БД")

                chain {
                    title = "Вычисление прав доступа"

                    worker {
                        on { state == CryptoState.RUNNING }

                        handle {
                            ordersRepoDone = ordersRepoRead.map { order ->
                                order.copy(principalRelations = order.resolveRelationsTo(principal))
                            }.toMutableList()

                            permitted =
                                ordersRepoDone.flatMap { it.principalRelations }.asSequence().flatMap { relation ->
                                    chainPermissions.map { permission ->
                                        AccessTableConditions(
                                            command = command,
                                            permission = permission,
                                            relation = relation,
                                        )
                                    }
                                }.any {
                                    accessTable[it] ?: false
                                }

                            if (permitted || ordersRepoRead.isEmpty()) {
                                ordersResponse = ordersRepoRead
                            } else {
                                fail(CryptoError(message = "User is not allowed to this operation"))
                            }
                        }
                    }
                }

                finishProcess("завершение процесса обработки запроса")
            }

        }.build()
    }
}
